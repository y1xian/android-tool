package com.yyxnb.lib.java.func;

/**
 * 两个参数的Supplier
 *
 * @param <T>  目标类型
 * @param <P1> 参数一类型
 * @param <P2> 参数二类型
 * @author yyx
 */
@FunctionalInterface
public interface Supplier2<T, P1, P2> {

	/**
	 * 生成实例的方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @return 目标对象
	 */
	T get(P1 p1, P2 p2);
}
