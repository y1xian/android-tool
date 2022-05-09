package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.convert.AbstractConverter;
import com.yyxnb.lib.java.convert.ConvertUtil;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * {@link AtomicIntegerArray}转换器
 *
 * @author yyx
 */
public class AtomicIntegerArrayConverter extends AbstractConverter<AtomicIntegerArray> {
	private static final long serialVersionUID = 1L;

	@Override
	protected AtomicIntegerArray convertInternal(Object value) {
		return new AtomicIntegerArray(ConvertUtil.convert(int[].class, value));
	}

}
