package com.yyxnb.android.modules;

/**
 * 全局配置
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/7
 */
public interface IConfig extends IModule {

	/**
	 * 是否debug模式
	 *
	 * @return {@code true or false}
	 */
	boolean isDebug();

	/**
	 * 是否打印log日志
	 *
	 * @param priority 打印级别，0或低级别则不打印
	 * @return {@code true or false}
	 */
	boolean enableLog(int priority);


}
