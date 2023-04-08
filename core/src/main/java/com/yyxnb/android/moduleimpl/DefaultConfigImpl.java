package com.yyxnb.android.moduleimpl;

import android.util.Log;

import com.yyxnb.android.BuildConfig;
import com.yyxnb.android.modules.IConfig;

/**
 * DefaultConfig
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/8
 */
public class DefaultConfigImpl implements IConfig {

	/**
	 * 是否debug模式
	 *
	 * @return {@code true or false}
	 */
	@Override
	public boolean isDebug() {
		return BuildConfig.DEBUG;
	}

	/**
	 * 是否打印log日志
	 *
	 * @param priority 打印级别，0或低级别则不打印
	 * @return {@code true or false}
	 */
	@Override
	public boolean enableLog(int priority) {
		if (priority == 0) {
			return false;
		}
		// 打印debug及以上的日志
		return priority >= Log.DEBUG;
	}

}
