package com.yyxnb.android.func;

/**
 * 函数对象<br>
 * 一个函数接口代表一个函数，用于包装一个函数为对象<br>
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/20
 */
@FunctionalInterface
public interface VoidFunc0 {

	/**
	 * 执行函数
	 *
	 * @throws Exception 自定义异常
	 */
	void call() throws Exception;

	/**
	 * 执行函数，异常包装为RuntimeException
	 */
	default void callWithRuntimeException() {
		try {
			call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
