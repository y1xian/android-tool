package com.yyxnb.android.func;

/**
 * 1参数Supplier
 *
 * @param <T>  目标类型
 * @param <P1> 参数一类型
 * @author yyx
 */
@FunctionalInterface
public interface Supplier1<T, P1> {

	/**
	 * 生成实例的方法
	 *
	 * @param p1 参数一
	 * @return 目标对象
	 */
	T get(P1 p1);
}
