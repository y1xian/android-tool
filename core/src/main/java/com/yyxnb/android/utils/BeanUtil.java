package com.yyxnb.android.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * BeanUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/12/12
 */
public class BeanUtil {

	/**
	 * 复制bean
	 *
	 * @param src              数据源
	 * @param dest             目标
	 * @param ignoreProperties 过滤字段
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
				if (UtilInner.equals(ignoreProperty, sourceField.getName())) {
					ignore = true;
					break;
				}
			}
			if (ignore) {
				continue;
			}
			String sourceFieldName = sourceField.getName();
			if (UtilInner.equalsIgnoreCase(sourceFieldName, "serialVersionUID")) {
				return;
			}
			Class<?> sourceFieldType = sourceField.getType();
			for (Field targetField : targetFields) {
				String targetFieldName = targetField.getName();
				Class<?> targetFieldType = targetField.getType();
				if (UtilInner.equals(sourceFieldName, targetFieldName) && sourceFieldType == targetFieldType) {
					try {
						sourceField.setAccessible(true);
						Method targetFieldMethod = dest.getClass().getMethod(
								String.format("set%s", UtilInner.upperFirst(targetField.getName())), targetFieldType);
						targetFieldMethod.invoke(dest, sourceField.get(src));
					} catch (Exception e) {
						UtilInner.e("复制属性异常", e);
					}
					break;
				}
			}
		}
	}

}
