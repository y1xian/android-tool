package com.yyxnb.android;

/**
 * 工具类异常
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/13
 */
public class UtilException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UtilException(Throwable e) {
		super(String.format("%s:%s", e.getClass().getSimpleName(), e.getMessage()), e);
	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String messageTemplate, Object... params) {
		super(String.format(messageTemplate, params));
	}

	public UtilException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public UtilException(Throwable throwable, String messageTemplate, Object... params) {
		super(String.format(messageTemplate, params), throwable);
	}
}
