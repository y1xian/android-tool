package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.CharsetUtil;
import com.yyxnb.lib.java.convert.AbstractConverter;

import java.nio.charset.Charset;

/**
 * 编码对象转换器
 *
 * @author yyx
 */
public class CharsetConverter extends AbstractConverter<Charset> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
