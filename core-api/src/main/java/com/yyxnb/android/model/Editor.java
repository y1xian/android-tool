package com.yyxnb.android.model;

/**
 * 编辑器接口，常用于对于集合中的元素做统一编辑<br>
 *
 * <pre>
 *     1、如果返回值为<code>null</code>，表示此值被抛弃
 *     2、对对象做修改
 * </pre>
 *
 * @author yyx
 * @date 2022/3/20
 */
@FunctionalInterface
public interface Editor<T> {
	/**
	 * 修改过滤后的结果
	 *
	 * @param t 被过滤的对象
	 * @return 修改后的对象，如果被过滤返回<code>null</code>
	 */
	T edit(T t);
}
