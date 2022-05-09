package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.convert.AbstractConverter;

import java.util.Locale;

/**
 * {@link Locale}对象转换器<br>
 * 只提供String转换支持
 *
 * @author yyx
 */
public class LocaleConverter extends AbstractConverter<Locale> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Locale convertInternal(Object value) {
		try {
			String str = convertToStr(value);
			if (StrUtil.isEmpty(str)) {
				return null;
			}

			final String[] items = str.split("_");
			if (items.length == 1) {
				return new Locale(items[0]);
			}
			if (items.length == 2) {
				return new Locale(items[0], items[1]);
			}
			return new Locale(items[0], items[1], items[2]);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
