package com.yyxnb.android.model;

/**
 * 匹配接口、匹配的对象类型
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
@FunctionalInterface
public interface Matcher<T> {

	/**
	 * 给定对象是否匹配
	 *
	 * @param t 对象
	 * @return 是否匹配
	 */
	boolean match(T t);
}