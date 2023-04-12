package com.yyxnb.android.modules;

/**
 * 接口模块，可注册配置
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/5/17
 */
public interface IModule {

	/**
	 * tag
	 *
	 * @return tag
	 */
	default String tag() {
		return getClass().getCanonicalName() + "@" + Integer.toHexString(hashCode());
	}
}
