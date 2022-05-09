package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.convert.AbstractConverter;
import com.yyxnb.lib.java.convert.ConvertUtil;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * {@link AtomicLongArray}转换器
 *
 * @author yyx
 */
public class AtomicLongArrayConverter extends AbstractConverter<AtomicLongArray> {
	private static final long serialVersionUID = 1L;

	@Override
	protected AtomicLongArray convertInternal(Object value) {
		return new AtomicLongArray(ConvertUtil.convert(long[].class, value));
	}

}
