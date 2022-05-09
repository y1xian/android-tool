package com.yyxnb.lib.java.convert.impl;

import com.yyxnb.lib.java.ClassLoaderUtil;
import com.yyxnb.lib.java.convert.AbstractConverter;

/**
 * 类转换器<br>
 * 将类名转换为类，默认初始化这个类（执行static块）
 *
 * @author yyx
 */
public class ClassConverter extends AbstractConverter<Class<?>> {
	private static final long serialVersionUID = 1L;

	private final boolean isInitialized;

	/**
	 * 构造
	 */
	public ClassConverter() {
		this(true);
	}

	/**
	 * 构造
	 *
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 */
	public ClassConverter(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	@Override
	protected Class<?> convertInternal(Object value) {
		return ClassLoaderUtil.loadClass(convertToStr(value), isInitialized);
	}

}
