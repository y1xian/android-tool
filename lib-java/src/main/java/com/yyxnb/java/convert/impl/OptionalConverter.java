package com.yyxnb.java.convert.impl;

import com.yyxnb.java.convert.AbstractConverter;

import java.util.Optional;

/**
 * {@link Optional}对象转换器
 *
 * @author yyx
 */
public class OptionalConverter extends AbstractConverter<Optional<?>> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Optional<?> convertInternal(Object value) {
		return Optional.ofNullable(value);
	}

}
