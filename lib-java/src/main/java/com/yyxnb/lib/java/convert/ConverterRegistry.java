package com.yyxnb.lib.java.convert;

import com.yyxnb.lib.java.ClassUtil;
import com.yyxnb.lib.java.ObjectUtil;
import com.yyxnb.lib.java.ReflectUtil;
import com.yyxnb.lib.java.ServiceLoaderUtil;
import com.yyxnb.lib.java.TypeUtil;
import com.yyxnb.lib.java.convert.impl.ArrayConverter;
import com.yyxnb.lib.java.convert.impl.AtomicBooleanConverter;
import com.yyxnb.lib.java.convert.impl.AtomicIntegerArrayConverter;
import com.yyxnb.lib.java.convert.impl.AtomicLongArrayConverter;
import com.yyxnb.lib.java.convert.impl.AtomicReferenceConverter;
import com.yyxnb.lib.java.convert.impl.BooleanConverter;
import com.yyxnb.lib.java.convert.impl.CharacterConverter;
import com.yyxnb.lib.java.convert.impl.CharsetConverter;
import com.yyxnb.lib.java.convert.impl.ClassConverter;
import com.yyxnb.lib.java.convert.impl.CollectionConverter;
import com.yyxnb.lib.java.convert.impl.CurrencyConverter;
import com.yyxnb.lib.java.convert.impl.EnumConverter;
import com.yyxnb.lib.java.convert.impl.LocaleConverter;
import com.yyxnb.lib.java.convert.impl.MapConverter;
import com.yyxnb.lib.java.convert.impl.NumberConverter;
import com.yyxnb.lib.java.convert.impl.OptionalConverter;
import com.yyxnb.lib.java.convert.impl.PathConverter;
import com.yyxnb.lib.java.convert.impl.PrimitiveConverter;
import com.yyxnb.lib.java.convert.impl.ReferenceConverter;
import com.yyxnb.lib.java.convert.impl.StringConverter;
import com.yyxnb.lib.java.convert.impl.TimeZoneConverter;
import com.yyxnb.lib.java.convert.impl.URIConverter;
import com.yyxnb.lib.java.convert.impl.URLConverter;
import com.yyxnb.lib.java.convert.impl.UUIDConverter;
import com.yyxnb.lib.java.lang.TypeReference;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * 转换器登记中心
 * <p>
 * 将各种类型Convert对象放入登记中心，通过convert方法查找目标类型对应的转换器，将被转换对象转换之。
 * </p>
 * <p>
 * 在此类中，存放着默认转换器和自定义转换器，默认转换器是Hutool中预定义的一些转换器，自定义转换器存放用户自定的转换器。
 * </p>
 *
 * @author yyx
 */
public class ConverterRegistry implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认类型转换器
	 */
	private Map<Type, Converter<?>> defaultConverterMap;
	/**
	 * 用户自定义类型转换器
	 */
	private volatile Map<Type, Converter<?>> customConverterMap;

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final ConverterRegistry INSTANCE = new ConverterRegistry();
	}

	/**
	 * 获得单例的 ConverterRegistry
	 *
	 * @return ConverterRegistry
	 */
	public static ConverterRegistry getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 构造
	 */
	public ConverterRegistry() {
		defaultConverter();
		putCustomBySpi();
	}

	/**
	 * 使用SPI加载转换器
	 */
	private void putCustomBySpi() {
		ServiceLoaderUtil.load(Converter.class).forEach(converter -> {
			try {
				Type type = TypeUtil.getTypeArgument(ClassUtil.getClass(converter));
				if (null != type) {
					putCustom(type, converter);
				}
			} catch (Exception e) {
				// 忽略注册失败的
			}
		});
	}

	/**
	 * 登记自定义转换器
	 *
	 * @param type           转换的目标类型
	 * @param converterClass 转换器类，必须有默认构造方法
	 * @return ConverterRegistry
	 */
	public ConverterRegistry putCustom(Type type, Class<? extends Converter<?>> converterClass) {
		return putCustom(type, ReflectUtil.newInstance(converterClass));
	}

	/**
	 * 登记自定义转换器
	 *
	 * @param type      转换的目标类型
	 * @param converter 转换器
	 * @return ConverterRegistry
	 */
	public ConverterRegistry putCustom(Type type, Converter<?> converter) {
		if (null == customConverterMap) {
			synchronized (this) {
				if (null == customConverterMap) {
					customConverterMap = new ConcurrentHashMap<>();
				}
			}
		}
		customConverterMap.put(type, converter);
		return this;
	}

	/**
	 * 获得转换器<br>
	 *
	 * @param <T>           转换的目标类型
	 * @param type          类型
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换器
	 */
	public <T> Converter<T> getConverter(Type type, boolean isCustomFirst) {
		Converter<T> converter;
		if (isCustomFirst) {
			converter = this.getCustomConverter(type);
			if (null == converter) {
				converter = this.getDefaultConverter(type);
			}
		} else {
			converter = this.getDefaultConverter(type);
			if (null == converter) {
				converter = this.getCustomConverter(type);
			}
		}
		return converter;
	}

	/**
	 * 获得默认转换器
	 *
	 * @param <T>  转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getDefaultConverter(Type type) {
		return (null == defaultConverterMap) ? null : (Converter<T>) defaultConverterMap.get(type);
	}

	/**
	 * 获得自定义转换器
	 *
	 * @param <T>  转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getCustomConverter(Type type) {
		return (null == customConverterMap) ? null : (Converter<T>) customConverterMap.get(type);
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T>           转换的目标类型（转换器转换到的类型）
	 * @param type          类型目标
	 * @param value         被转换值
	 * @param defaultValue  默认值
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(Type type, Object value, T defaultValue, boolean isCustomFirst) throws ConvertException {
		if (TypeUtil.isUnknown(type) && null == defaultValue) {
			// 对于用户不指定目标类型的情况，返回原值
			return (T) value;
		}
		if (ObjectUtil.isNull(value)) {
			return defaultValue;
		}
		if (TypeUtil.isUnknown(type)) {
			type = defaultValue.getClass();
		}

		if (type instanceof TypeReference) {
			type = ((TypeReference<?>) type).getType();
		}

		// 标准转换器
		final Converter<T> converter = getConverter(type, isCustomFirst);
		if (null != converter) {
			return converter.convert(value, defaultValue);
		}

		Class<T> rowType = (Class<T>) TypeUtil.getClass(type);
		if (null == rowType) {
			if (null != defaultValue) {
				rowType = (Class<T>) defaultValue.getClass();
			} else {
				// 无法识别的泛型类型，按照Object处理
				return (T) value;
			}
		}

		// 特殊类型转换，包括Collection、Map、强转、Array等
		final T result = convertSpecial(type, rowType, value, defaultValue);
		if (null != result) {
			return result;
		}

		// 尝试转Bean
//		if (BeanUtil.isBean(rowType)) {
//			return new BeanConverter<T>(type).convert(value, defaultValue);
//		}

		// 无法转换
		throw new ConvertException("Can not Converter from [%s] to [%s]", value.getClass().getName(), type);
	}

	/**
	 * 转换值为指定类型<br>
	 * 自定义转换器优先
	 *
	 * @param <T>          转换的目标类型（转换器转换到的类型）
	 * @param type         类型
	 * @param value        值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Type type, Object value, T defaultValue) throws ConvertException {
		return convert(type, value, defaultValue, true);
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T>   转换的目标类型（转换器转换到的类型）
	 * @param type  类型
	 * @param value 值
	 * @return 转换后的值，默认为{@code null}
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Type type, Object value) throws ConvertException {
		return convert(type, value, null);
	}

	// ----------------------------------------------------------- Private method start

	/**
	 * 特殊类型转换<br>
	 * 包括：
	 *
	 * <pre>
	 * Collection
	 * Map
	 * 强转（无需转换）
	 * 数组
	 * </pre>
	 *
	 * @param <T>          转换的目标类型（转换器转换到的类型）
	 * @param type         类型
	 * @param value        值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	private <T> T convertSpecial(Type type, Class<T> rowType, Object value, T defaultValue) {
		if (null == rowType) {
			return null;
		}

		// 集合转换（不可以默认强转）
		if (Collection.class.isAssignableFrom(rowType)) {
			final CollectionConverter collectionConverter = new CollectionConverter(type);
			return (T) collectionConverter.convert(value, (Collection<?>) defaultValue);
		}

		// Map类型（不可以默认强转）
		if (Map.class.isAssignableFrom(rowType)) {
			final MapConverter mapConverter = new MapConverter(type);
			return (T) mapConverter.convert(value, (Map<?, ?>) defaultValue);
		}

		// 默认强转
		if (rowType.isInstance(value)) {
			return (T) value;
		}

		// 枚举转换
		if (rowType.isEnum()) {
			return (T) new EnumConverter(rowType).convert(value, defaultValue);
		}

		// 数组转换
		if (rowType.isArray()) {
			final ArrayConverter arrayConverter = new ArrayConverter(rowType);
			return (T) arrayConverter.convert(value, defaultValue);
		}

		// 表示非需要特殊转换的对象
		return null;
	}

	/**
	 * 注册默认转换器
	 *
	 * @return 转换器
	 */
	private ConverterRegistry defaultConverter() {
		defaultConverterMap = new ConcurrentHashMap<>();

		// 原始类型转换器
		defaultConverterMap.put(int.class, new PrimitiveConverter(int.class));
		defaultConverterMap.put(long.class, new PrimitiveConverter(long.class));
		defaultConverterMap.put(byte.class, new PrimitiveConverter(byte.class));
		defaultConverterMap.put(short.class, new PrimitiveConverter(short.class));
		defaultConverterMap.put(float.class, new PrimitiveConverter(float.class));
		defaultConverterMap.put(double.class, new PrimitiveConverter(double.class));
		defaultConverterMap.put(char.class, new PrimitiveConverter(char.class));
		defaultConverterMap.put(boolean.class, new PrimitiveConverter(boolean.class));

		// 包装类转换器
		defaultConverterMap.put(Number.class, new NumberConverter());
		defaultConverterMap.put(Integer.class, new NumberConverter(Integer.class));
		defaultConverterMap.put(AtomicInteger.class, new NumberConverter(AtomicInteger.class));
		defaultConverterMap.put(Long.class, new NumberConverter(Long.class));
		defaultConverterMap.put(LongAdder.class, new NumberConverter(LongAdder.class));
		defaultConverterMap.put(AtomicLong.class, new NumberConverter(AtomicLong.class));
		defaultConverterMap.put(Byte.class, new NumberConverter(Byte.class));
		defaultConverterMap.put(Short.class, new NumberConverter(Short.class));
		defaultConverterMap.put(Float.class, new NumberConverter(Float.class));
		defaultConverterMap.put(Double.class, new NumberConverter(Double.class));
		defaultConverterMap.put(DoubleAdder.class, new NumberConverter(DoubleAdder.class));
		defaultConverterMap.put(Character.class, new CharacterConverter());
		defaultConverterMap.put(Boolean.class, new BooleanConverter());
		defaultConverterMap.put(AtomicBoolean.class, new AtomicBooleanConverter());
		defaultConverterMap.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
		defaultConverterMap.put(BigInteger.class, new NumberConverter(BigInteger.class));
		defaultConverterMap.put(CharSequence.class, new StringConverter());
		defaultConverterMap.put(String.class, new StringConverter());

		// URI and URL
		defaultConverterMap.put(URI.class, new URIConverter());
		defaultConverterMap.put(URL.class, new URLConverter());

		// Reference
		defaultConverterMap.put(WeakReference.class, new ReferenceConverter(WeakReference.class));// since 3.0.8
		defaultConverterMap.put(SoftReference.class, new ReferenceConverter(SoftReference.class));// since 3.0.8
		defaultConverterMap.put(AtomicReference.class, new AtomicReferenceConverter());// since 3.0.8

		//AtomicXXXArray
		defaultConverterMap.put(AtomicIntegerArray.class, new AtomicIntegerArrayConverter());
		defaultConverterMap.put(AtomicLongArray.class, new AtomicLongArrayConverter());

		// 其它类型
		defaultConverterMap.put(Class.class, new ClassConverter());
		defaultConverterMap.put(TimeZone.class, new TimeZoneConverter());
		defaultConverterMap.put(Locale.class, new LocaleConverter());
		defaultConverterMap.put(Charset.class, new CharsetConverter());
		defaultConverterMap.put(Path.class, new PathConverter());
		defaultConverterMap.put(Currency.class, new CurrencyConverter());// since 3.0.8
		defaultConverterMap.put(UUID.class, new UUIDConverter());// since 4.0.10
		defaultConverterMap.put(Optional.class, new OptionalConverter());// since 5.0.0

		return this;
	}
}
