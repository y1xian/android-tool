package com.yyxnb.lib.java.exceptions;

import com.yyxnb.lib.java.StrUtil;

/**
 * 工具类异常
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/20
 */
public class UtilException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public UtilException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public UtilException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
