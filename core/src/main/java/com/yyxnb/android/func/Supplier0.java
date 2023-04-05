package com.yyxnb.android.func;

/**
 * 无参Supplier，由于{@link java.util.function.Supplier}需要api24，故此弄了个Supplier0
 *
 * @param <T> 目标类型
 * @author yyx
 */
@FunctionalInterface
public interface Supplier0<T> {

	/**
	 * 生成实例的方法
	 *
	 * @return 目标对象
	 */
	T get();
}
