package com.yyxnb.java.collection;

/**
 * 过滤器接口
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/20
 */
@FunctionalInterface
public interface Filter<T> {
	/**
	 * 是否接受对象
	 *
	 * @param t 检查的对象
	 * @return 是否接受对象
	 */
	boolean accept(T t);
}