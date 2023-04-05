package com.yyxnb.android.func;

/**
 * 函数对象<br>
 * 一个函数接口代表一个函数，用于包装一个函数为对象<br>
 *
 * <pre>
 * </pre>
 *
 * @param <P> 参数类型
 * @author yyx
 * @date 2022/3/20
 */
@FunctionalInterface
public interface VoidFunc1<P> {

	/**
	 * 执行函数
	 *
	 * @param parameter 参数
	 * @throws Exception 自定义异常
	 */
	void call(P parameter) throws Exception;

	/**
	 * 执行函数，异常包装为RuntimeException
	 *
	 * @param parameter 参数
	 */
	default void callWithRuntimeException(P parameter) {
		try {
			call(parameter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
