package com.yyxnb.java.convert;

import com.yyxnb.java.StrUtil;
import com.yyxnb.java.exceptions.ExceptionUtil;

/**
 * 转换异常
 *
 * @author yyx
 */
public class ConvertException extends RuntimeException {
	private static final long serialVersionUID = 4730597402855274362L;

	public ConvertException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ConvertException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ConvertException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}