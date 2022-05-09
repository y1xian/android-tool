package com.yyxnb.lib.andorid.log;

import android.text.TextUtils;

import com.yyxnb.lib.andorid.app.AppUtil;

/**
 * Log配置
 *
 * @author yyx
 * @date 2022/4/6
 */
public class LogConfig {

	/**
	 * 打印堆栈信息
	 */
	private boolean showThreadInfo = true;
	/**
	 * 是否打印日志
	 */
	private boolean showLog = AppUtil.isDebug();
	/**
	 * 简洁
	 */
	private boolean simpleLog = true;
	/**
	 * tag标识
	 */
	private String tag = ">----What---->";

	public LogConfig setTag(String tag) {
		if (!TextUtils.isEmpty(tag)) {
			this.tag = tag;
		}
		return this;
	}

	public LogConfig setShowThreadInfo(boolean showThreadInfo) {
		this.showThreadInfo = showThreadInfo;
		return this;
	}

	public LogConfig setShowLog(boolean showLog) {
		this.showLog = showLog;
		return this;
	}

	public LogConfig setSimpleLog(boolean simpleLog) {
		this.simpleLog = simpleLog;
		return this;
	}

	public String getTag() {
		return tag;
	}

	public boolean isShowLog() {
		return showLog;
	}

	public boolean isShowThreadInfo() {
		return showThreadInfo;
	}

	public boolean isSimpleLog() {
		return simpleLog;
	}

}