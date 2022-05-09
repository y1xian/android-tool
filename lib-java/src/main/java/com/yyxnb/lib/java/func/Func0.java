package com.yyxnb.lib.java.func;

/**
 * 无参数的函数对象<br>
 * 一个函数接口代表一个函数，用于包装一个函数为对象<br>
 *
 * <pre>
 * </pre>
 *
 * @param <R> 返回值类型
 * @author yyx
 * @date 2022/3/20
 */
@FunctionalInterface
public interface Func0<R> {

	/**
	 * 执行函数
	 *
	 * @return 函数执行结果
	 * @throws Exception 自定义异常
	 */
	R call() throws Exception;

	/**
	 * 执行函数，异常包装为RuntimeException
	 *
	 * @return 函数执行结果
	 */
	default R callWithRuntimeException() {
		try {
			return call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
