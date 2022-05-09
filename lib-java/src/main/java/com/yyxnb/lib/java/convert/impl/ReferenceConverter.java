package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.TypeUtil;
import com.yyxnb.lib.java.convert.AbstractConverter;
import com.yyxnb.lib.java.convert.ConverterRegistry;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * {@link Reference}转换器
 *
 * @author yyx
 */
@SuppressWarnings("rawtypes")
public class ReferenceConverter extends AbstractConverter<Reference> {
	private static final long serialVersionUID = 1L;

	private final Class<? extends Reference> targetType;

	/**
	 * 构造
	 *
	 * @param targetType {@link Reference}实现类型
	 */
	public ReferenceConverter(Class<? extends Reference> targetType) {
		this.targetType = targetType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Reference<?> convertInternal(Object value) {

		//尝试将值转换为Reference泛型的类型
		Object targetValue = null;
		final Type paramType = TypeUtil.getTypeArgument(targetType);
		if (!TypeUtil.isUnknown(paramType)) {
			targetValue = ConverterRegistry.getInstance().convert(paramType, value);
		}
		if (null == targetValue) {
			targetValue = value;
		}

		if (this.targetType == WeakReference.class) {
			return new WeakReference(targetValue);
		} else if (this.targetType == SoftReference.class) {
			return new SoftReference(targetValue);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Reference type: %s", this.targetType.getName()));
	}

}
