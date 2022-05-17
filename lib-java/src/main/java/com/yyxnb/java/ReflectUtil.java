package com.yyxnb.java;

import com.yyxnb.java.collection.ArrayUtil;
import com.yyxnb.java.convert.ConvertUtil;
import com.yyxnb.java.exceptions.UtilException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 反射工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class ReflectUtil {


	/**
	 * 构造对象缓存
	 */
	private static final SimpleCache<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new SimpleCache<>();
	/**
	 * 字段缓存
	 */
	private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
	/**
	 * 方法缓存
	 */
	private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();


	/**
	 * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
	 * <p>
	 * 对于某些特殊的接口，按照其默认实现实例化，例如：
	 * <pre>
	 *     Map       -》 HashMap
	 *     Collction -》 ArrayList
	 *     List      -》 ArrayList
	 *     Set       -》 HashSet
	 * </pre>
	 *
	 * @param <T>       对象类型
	 * @param beanClass 被构造的类
	 * @return 构造后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceIfPossible(Class<T> beanClass) {
		// 某些特殊接口的实例化按照默认实现进行
		if (beanClass.isAssignableFrom(AbstractMap.class)) {
			beanClass = (Class<T>) HashMap.class;
		} else if (beanClass.isAssignableFrom(List.class)) {
			beanClass = (Class<T>) ArrayList.class;
		} else if (beanClass.isAssignableFrom(Set.class)) {
			beanClass = (Class<T>) HashSet.class;
		}

		try {
			return newInstance(beanClass);
		} catch (Exception e) {
			// ignore
			// 默认构造不存在的情况下查找其它构造
		}

		final Constructor<T>[] constructors = getConstructors(beanClass);
		Class<?>[] parameterTypes;
		for (Constructor<T> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			if (0 == parameterTypes.length) {
				continue;
			}
			setAccessible(constructor);
			try {
				return constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
			} catch (Exception ignore) {
				// 构造出错时继续尝试下一种构造方式
			}
		}
		return null;
	}

	// ----------------------------------------------------------------------- newInstance

	/**
	 * 实例化对象
	 *
	 * @param <T>   对象类型
	 * @param clazz 类名
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) throws UtilException {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (Exception e) {
			throw new UtilException(e, "Instance class [%s] error!", clazz);
		}
	}

	/**
	 * 实例化对象
	 *
	 * @param <T>    对象类型
	 * @param clazz  类
	 * @param params 构造函数参数
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) throws UtilException {
		if (ArrayUtil.isEmpty(params)) {
			final Constructor<T> constructor = getConstructor(clazz);
			try {
				return constructor.newInstance();
			} catch (Exception e) {
				throw new UtilException(e, "Instance class [%s] error!", clazz);
			}
		}

		final Class<?>[] paramTypes = ClassUtil.getClasses(params);
		final Constructor<T> constructor = getConstructor(clazz, paramTypes);
		if (null == constructor) {
			throw new UtilException("No Constructor matched for parameter types: [%s]", new Object[]{paramTypes});
		}
		try {
			return constructor.newInstance(params);
		} catch (Exception e) {
			throw new UtilException(e, "Instance class [%s] error!", clazz);
		}
	}


	// ----------------------------------------------------------------------- Constructor

	/**
	 * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为true
	 *
	 * @param <T>            对象类型
	 * @param clazz          类
	 * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可，此参数可以不传
	 * @return 构造方法，如果未找到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
		if (null == clazz) {
			return null;
		}

		final Constructor<?>[] constructors = getConstructors(clazz);
		Class<?>[] pts;
		for (Constructor<?> constructor : constructors) {
			pts = constructor.getParameterTypes();
			if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
				// 构造可访问
				setAccessible(constructor);
				return (Constructor<T>) constructor;
			}
		}
		return null;
	}

	/**
	 * 获得一个类中所有构造列表
	 *
	 * @param <T>       构造的对象类型
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
		Constructor<?>[] constructors = CONSTRUCTORS_CACHE.get(beanClass);
		if (null != constructors) {
			return (Constructor<T>[]) constructors;
		}

		constructors = getConstructorsDirectly(beanClass);
		return (Constructor<T>[]) CONSTRUCTORS_CACHE.put(beanClass, constructors);
	}

	/**
	 * 获得一个类中所有构造列表，直接反射获取，无缓存
	 *
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
		return beanClass.getDeclaredConstructors();
	}

	// -----------------------------------------------------------------------

	/**
	 * 设置方法为可访问（私有方法可以被外部调用）
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 */
	public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
		if (null != accessibleObject && !accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}

	// ----------------------------------------------------------------------- Field

	/**
	 * 查找指定类中的指定name的字段（包括非public字段），也包括父类和Object类的字段， 字段不存在则返回{@code null}
	 *
	 * @param beanClass 被查找字段的类,不能为null
	 * @param name      字段名
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field getField(Class<?> beanClass, String name) throws SecurityException {
		final Field[] fields = getFields(beanClass);
		return ArrayUtil.firstMatch((field) -> name.equals(getFieldName(field)), fields);
	}

	/**
	 * 获得一个类中所有字段列表，包括其父类中的字段<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Field[] getFields(Class<?> beanClass) throws SecurityException {
		Field[] allFields = FIELDS_CACHE.get(beanClass);
		if (null != allFields) {
			return allFields;
		}

		allFields = getFieldsDirectly(beanClass, true);
		return FIELDS_CACHE.put(beanClass, allFields);
	}

	/**
	 * 获得一个类中所有字段列表，直接反射获取，无缓存<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass            类
	 * @param withSuperClassFields 是否包括父类的字段列表
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Field[] getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFields) throws SecurityException {
		Field[] allFields = null;
		Class<?> searchType = beanClass;
		Field[] declaredFields;
		while (searchType != null) {
			declaredFields = searchType.getDeclaredFields();
			if (null == allFields) {
				allFields = declaredFields;
			} else {
				allFields = ArrayUtil.append(allFields, declaredFields);
			}
			searchType = withSuperClassFields ? searchType.getSuperclass() : null;
		}

		return allFields;
	}


	/**
	 * 获取字段名，如果存在{@link Alias}注解，读取注解的值作为名称
	 *
	 * @param field 字段
	 * @return 字段名
	 */
	public static String getFieldName(Field field) {
		if (null == field) {
			return null;
		}

		final Alias alias = field.getAnnotation(Alias.class);
		if (null != alias) {
			return alias.value();
		}

		return field.getName();
	}


	/**
	 * 获取字段值
	 *
	 * @param obj       对象，如果static字段，此处为类
	 * @param fieldName 字段名
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getFieldValue(Object obj, String fieldName) throws UtilException {
		if (null == obj || StrUtil.isBlank(fieldName)) {
			return null;
		}
		return getFieldValue(obj, getField(obj instanceof Class ? (Class<?>) obj : obj.getClass(), fieldName));
	}

	/**
	 * 获取静态字段值
	 *
	 * @param field 字段
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getStaticFieldValue(Field field) throws UtilException {
		return getFieldValue(null, field);
	}

	/**
	 * 获取字段值
	 *
	 * @param obj   对象，static字段则此字段为null
	 * @param field 字段
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getFieldValue(Object obj, Field field) throws UtilException {
		if (null == field) {
			return null;
		}
		if (obj instanceof Class) {
			// 静态字段获取时对象为null
			obj = null;
		}

		setAccessible(field);
		Object result;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for %s.%s", field.getDeclaringClass(), field.getName());
		}
		return result;
	}

	/**
	 * 获取所有字段的值
	 *
	 * @param obj bean对象，如果是static字段，此处为类class
	 * @return 字段值数组
	 */
	public static Object[] getFieldsValue(Object obj) {
		if (null != obj) {
			final Field[] fields = getFields(obj instanceof Class ? (Class<?>) obj : obj.getClass());
			if (null != fields) {
				final Object[] values = new Object[fields.length];
				for (int i = 0; i < fields.length; i++) {
					values[i] = getFieldValue(obj, fields[i]);
				}
				return values;
			}
		}
		return null;
	}

	/**
	 * 设置字段值
	 *
	 * @param obj       对象,static字段则此处传Class
	 * @param fieldName 字段名
	 * @param value     值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) throws UtilException {
		Assert.notNull(obj);
		Assert.notBlank(fieldName);

		final Field field = getField((obj instanceof Class) ? (Class<?>) obj : obj.getClass(), fieldName);
		Assert.notNull(field, "Field [%s] is not exist in [%s]", fieldName, obj.getClass().getName());
		setFieldValue(obj, field, value);
	}

	/**
	 * 设置字段值
	 *
	 * @param obj   对象，如果是static字段，此参数为null
	 * @param field 字段
	 * @param value 值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(Object obj, Field field, Object value) throws UtilException {
		Assert.notNull(field, "Field in [%s] not exist !", obj);

		final Class<?> fieldType = field.getType();
		if (null != value) {
			if (!fieldType.isAssignableFrom(value.getClass())) {
				//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
				final Object targetValue = ConvertUtil.convert(fieldType, value);
				if (null != targetValue) {
					value = targetValue;
				}
			}
		} else {
			// 获取null对应默认值，防止原始类型造成空指针问题
			value = ClassUtil.getDefaultValue(fieldType);
		}

		setAccessible(field);
		try {
			field.set(obj instanceof Class ? null : obj, value);
		} catch (IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for %s.%s", obj, field.getName());
		}
	}

	// ----------------------------------------------------------------------- invoke

	/**
	 * 执行静态方法
	 *
	 * @param <T>    对象类型
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws UtilException 多种异常包装
	 */
	public static <T> T invokeStatic(Method method, Object... args) throws UtilException {
		return invoke(null, method, args);
	}

	/**
	 * 执行对象中指定方法
	 * 如果需要传递的参数为null,请使用NullWrapperBean来传递,不然会丢失类型信息
	 *
	 * @param <T>        返回对象类型
	 * @param obj        方法所在对象
	 * @param methodName 方法名
	 * @param args       参数列表
	 * @return 执行结果
	 * @throws UtilException IllegalAccessException包装
	 * @see NullWrapperBean
	 */
	public static <T> T invoke(Object obj, String methodName, Object... args) throws UtilException {
		Assert.notNull(obj, "Object to get method must be not null!");
		Assert.notBlank(methodName, "Method name must be not blank!");

		final Method method = getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new UtilException("No such method: [%s] from [%s]", methodName, obj.getClass());
		}
		return invoke(obj, method, args);
	}

	/**
	 * 执行方法
	 *
	 * <p>
	 * 对于用户传入参数会做必要检查，包括：
	 *
	 * <pre>
	 *     1、忽略多余的参数
	 *     2、参数不够补齐默认值
	 *     3、传入参数为null，但是目标参数类型为原始类型，做转换
	 * </pre>
	 *
	 * @param <T>    返回对象类型
	 * @param obj    对象，如果执行静态方法，此值为{@code null}
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws UtilException 一些列异常的包装
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object obj, Method method, Object... args) throws UtilException {
		setAccessible(method);

		// 检查用户传入参数：
		// 1、忽略多余的参数
		// 2、参数不够补齐默认值
		// 3、通过NullWrapperBean传递的参数,会直接赋值null
		// 4、传入参数为null，但是目标参数类型为原始类型，做转换
		// 5、传入参数类型不对应，尝试转换类型
		final Class<?>[] parameterTypes = method.getParameterTypes();
		final Object[] actualArgs = new Object[parameterTypes.length];
		if (null != args) {
			for (int i = 0; i < actualArgs.length; i++) {
				if (i >= args.length || null == args[i]) {
					// 越界或者空值
					actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
				} else if (args[i] instanceof NullWrapperBean) {
					//如果是通过NullWrapperBean传递的null参数,直接赋值null
					actualArgs[i] = null;
				} else if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
					//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
					final Object targetValue = ConvertUtil.convert(parameterTypes[i], args[i]);
					if (null != targetValue) {
						actualArgs[i] = targetValue;
					}
				} else {
					actualArgs[i] = args[i];
				}
			}
		}

//		if (method.isDefault()) {
//			// 当方法是default方法时，尤其对象是代理对象，需使用句柄方式执行
//			// 代理对象情况下调用method.invoke会导致循环引用执行，最终栈溢出
//			return MethodHandleUtil.invokeSpecial(obj, method, args);
//		}

		try {
			return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	// ----------------------------------------------------------------------- Method

	/**
	 * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
	 *
	 * <p>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。
	 * </p>
	 *
	 * @param obj        被查找的对象，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param args       参数
	 * @return 方法
	 * @throws SecurityException 无访问权限抛出异常
	 */
	public static Method getMethodOfObj(Object obj, String methodName, Object... args) throws SecurityException {
		if (null == obj || StrUtil.isBlank(methodName)) {
			return null;
		}
		return getMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
	}

	/**
	 * 查找指定方法 如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
		return getMethod(clazz, false, methodName, paramTypes);
	}

	/**
	 * 查找指定方法 如果找不到对应的方法则返回{@code null}<br>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。<br>
	 * 如果查找的方法有多个同参数类型重载，查找第一个找到的方法
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param ignoreCase 是否忽略大小写
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		final Method[] methods = clazz.getMethods();
		if (ArrayUtil.isNotEmpty(methods)) {
			for (Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
						&& ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
						//排除桥接方法，pr#1965@Github
						&& !method.isBridge()) {
					return method;
				}
			}
		}
		return null;
	}

}
