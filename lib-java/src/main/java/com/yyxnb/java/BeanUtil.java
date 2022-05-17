package com.yyxnb.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/20
 */
public class BeanUtil {

	/**
	 * 复制Bean对象属性
	 *
	 * @param src              源Bean对象
	 * @param dest             目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 */
	public static void copyProperties(Object src, Object dest, String... ignoreProperties) {
		if (src == null || dest == null) {
			return;
		}
		Field[] sourceFields = src.getClass().getDeclaredFields();
		Field[] targetFields = dest.getClass().getDeclaredFields();
		for (Field sourceField : sourceFields) {
			boolean ignore = false;
			for (String ignoreProperty : ignoreProperties) {
				if (StrUtil.equals(ignoreProperty, sourceField.getName())) {
					ignore = true;
					break;
				}
			}
			if (ignore) {
				continue;
			}
			String sourceFieldName = sourceField.getName();
			Class<?> sourceFieldType = sourceField.getType();
			for (Field targetField : targetFields) {
				String targetFieldName = targetField.getName();
				Class<?> targetFieldType = targetField.getType();
				if (StrUtil.equals(sourceFieldName, targetFieldName) && sourceFieldType == targetFieldType) {
					try {
						sourceField.setAccessible(true);
						Method targetFieldMethod = dest.getClass().getMethod(StrUtil.format("set%s", StrUtil.upperFirst(targetField.getName())), targetFieldType);
						targetFieldMethod.invoke(dest, sourceField.get(src));
					} catch (Exception e) {
					}
					break;
				}
			}
		}
	}

	/**
	 * 复制Bean对象属性
	 *
	 * @param src              源Bean对象
	 * @param dest             目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 * @param <T>              对象类型
	 * @return 目标对象
	 */
	public static <T> T copyProperties(Object src, Class<T> dest, String... ignoreProperties) {
		T target = ReflectUtil.newInstanceIfPossible(dest);
		if (null == src) {
			return target;
		}
		copyProperties(src, target, ignoreProperties);
		return target;
	}
}
