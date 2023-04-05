package com.yyxnb.android.modules;

/**
 * ILog
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/12/2
 */
public interface ILog extends IModule{

	/**
	 * Log.VERBOSE 全部日志
	 *
	 * @param contents 内容
	 */
	void v(Object... contents);

	/**
	 * Log.VERBOSE 全部日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	void vTag(String tag, Object... contents);

	/**
	 * Log.DEBUG 调试日志
	 *
	 * @param contents 内容
	 */
	void d(Object... contents);

	/**
	 * Log.DEBUG 调试日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	void dTag(String tag, Object... contents);

	/**
	 * Log.INFO 信息日志
	 *
	 * @param contents 内容
	 */
	void i(Object... contents);

	/**
	 * Log.INFO 信息日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	void iTag(String tag, Object... contents);

	/**
	 * Log.WARN 警告日志
	 *
	 * @param contents 内容
	 */
	void w(Object... contents);

	/**
	 * Log.WARN 警告日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	void wTag(String tag, Object... contents);

	/**
	 * Log.ERROR 错误日志
	 *
	 * @param contents 内容
	 */
	void e(Object... contents);

	/**
	 * Log.ERROR 错误日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	void eTag(String tag, Object... contents);

}
