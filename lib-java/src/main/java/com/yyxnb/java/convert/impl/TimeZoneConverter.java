package com.yyxnb.java.convert.impl;

import com.yyxnb.java.convert.AbstractConverter;

import java.util.TimeZone;

/**
 * TimeZone转换器
 *
 * @author yyx
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone> {
	private static final long serialVersionUID = 1L;

	@Override
	protected TimeZone convertInternal(Object value) {
		return TimeZone.getTimeZone(convertToStr(value));
	}

}
