package com.yyxnb.java.convert.impl;

import com.yyxnb.java.TypeUtil;
import com.yyxnb.java.convert.AbstractConverter;
import com.yyxnb.java.convert.ConverterRegistry;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link AtomicReference}转换器
 *
 * @author yyx
 */
@SuppressWarnings("rawtypes")
public class AtomicReferenceConverter extends AbstractConverter<AtomicReference> {
	private static final long serialVersionUID = 1L;

	@Override
	protected AtomicReference<?> convertInternal(Object value) {

		//尝试将值转换为Reference泛型的类型
		Object targetValue = null;
		final Type paramType = TypeUtil.getTypeArgument(AtomicReference.class);
		if(!TypeUtil.isUnknown(paramType)){
			targetValue = ConverterRegistry.getInstance().convert(paramType, value);
		}
		if(null == targetValue){
			targetValue = value;
		}

		return new AtomicReference<>(targetValue);
	}

}
