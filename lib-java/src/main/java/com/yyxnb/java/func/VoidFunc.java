package com.yyxnb.java.func;

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
public interface VoidFunc<P> {

	/**
	 * 执行函数
	 *
	 * @param parameters 参数列表
	 * @throws Exception 自定义异常
	 */
	@SuppressWarnings("unchecked")
	void call(P... parameters) throws Exception;

	/**
	 * 执行函数，异常包装为RuntimeException
	 *
	 * @param parameters 参数列表
	 */
	@SuppressWarnings("unchecked")
	default void callWithRuntimeException(P... parameters) {
		try {
			call(parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
