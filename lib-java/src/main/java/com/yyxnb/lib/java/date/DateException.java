package com.yyxnb.lib.java.date;

import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.exceptions.ExceptionUtil;

/**
 * 工具类异常
 *
 * @author yyx
 */
public class DateException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public DateException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DateException(String message) {
		super(message);
	}

	public DateException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DateException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
