package com.yyxnb.android.log;

import android.util.Log;

import com.yyxnb.android.interfaces.ILog;

import java.util.List;
import java.util.Map;

public class AndroidLog implements ILog {

	private static final String TAG = AndroidLog.class.getSimpleName();
	private static volatile AndroidLog sInstance = null;

	public static AndroidLog getInstance() {
		if (null == sInstance) {
			synchronized (AndroidLog.class) {
				if (null == sInstance) {
					sInstance = new AndroidLog();
				}
			}
		}
		return sInstance;
	}

	/**
	 * 配置项
	 *
	 * @return {@link LogConfig}
	 */
	@Override
	public LogConfig init() {
		return null;
	}

	/**
	 * 返回最后的Log
	 *
	 * @return 结果
	 */
	@Override
	public String getLastLog() {
		return null;
	}

	/**
	 * Log.DEBUG
	 *
	 * @param message 内容
	 * @param args    String.format(message,args)
	 */
	@Override
	public void d(String message, Object... args) {
		Log.d(TAG, String.format(message, args));
	}

	/**
	 * Log.ERROR
	 *
	 * @param message 内容
	 * @param args    String.format(message,args)
	 */
	@Override
	public void e(String message, Object... args) {
		Log.e(TAG, String.format(message, args));
	}

	/**
	 * Log.ERROR
	 *
	 * @param throwable throwable
	 * @param message   内容
	 * @param args      String.format(message,args)
	 */
	@Override
	public void e(Throwable throwable, String message, Object... args) {
		Log.e(TAG, String.format(message, args), throwable);
	}

	/**
	 * Log.WARN
	 *
	 * @param message 内容
	 * @param args    String.format(message,args)
	 */
	@Override
	public void w(String message, Object... args) {
		Log.w(TAG, String.format(message, args));
	}

	/**
	 * Log.INFO
	 *
	 * @param message 内容
	 * @param args    String.format(message,args)
	 */
	@Override
	public void i(String message, Object... args) {
		Log.i(TAG, String.format(message, args));
	}

	/**
	 * Log.VERBOSE
	 *
	 * @param message 内容
	 * @param args    String.format(message,args)
	 */
	@Override
	public void v(String message, Object... args) {
		Log.v(TAG, String.format(message, args));
	}

	/**
	 * Log.ASSERT
	 *
	 * @param message 内容
	 * @param args    String.format(message,args)
	 */
	@Override
	public void a(String message, Object... args) {
		Log.d(TAG, String.format(message, args));
	}

	/**
	 * 打印Json
	 *
	 * @param json Json数据
	 */
	@Override
	public void json(String json) {

	}

	/**
	 * 打印xml
	 *
	 * @param xml xml数据
	 */
	@Override
	public void xml(String xml) {

	}

	/**
	 * 打印map
	 *
	 * @param map map数据
	 */
	@Override
	public void map(Map<?, ?> map) {

	}

	/**
	 * 打印list
	 *
	 * @param list list数据
	 */
	@Override
	public void list(List<?> list) {

	}
}
