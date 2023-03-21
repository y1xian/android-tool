package com.yyxnb.android.model;

import java.io.Serializable;

/**
 * 建造者模式接口定义
 *
 * <pre>
 * </pre>
 *
 * @param <T> 建造对象类型
 * @author yyx
 * @date 2022/3/25
 */
public interface Builder<T> extends Serializable {
	/**
	 * 构建
	 *
	 * @return 被构建的对象
	 */
	T build();
}