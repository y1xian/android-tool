package com.yyxnb.android.log;

import android.util.Log;

import androidx.annotation.RestrictTo;

import com.yyxnb.android.constant.StringConstants;
import com.yyxnb.android.modules.ILog;
import com.yyxnb.android.utils.LogUtil;

/**
 * 框架默认打印
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/12/2
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LogImpl implements ILog {

	private static final String TAG = LogImpl.class.getSimpleName();
	private static final String DOUBLE_DIVIDER = "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
	private static final String SINGLE_DIVIDER = "──────────────────────────────────────────────";
	public static String LINE_SEPARATOR = StringConstants.CRLF;

	/**
	 * Log.VERBOSE 全部日志
	 *
	 * @param contents 内容
	 */
	@Override
	public void v(Object... contents) {
		vTag(TAG, contents);
	}

	/**
	 * Log.VERBOSE 全部日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	@Override
	public void vTag(String tag, Object... contents) {
		log(Log.VERBOSE, tag, contents);
	}

	/**
	 * Log.DEBUG 调试日志
	 *
	 * @param contents 内容
	 */
	@Override
	public void d(Object... contents) {
		dTag(TAG, contents);
	}

	/**
	 * Log.DEBUG 调试日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	@Override
	public void dTag(String tag, Object... contents) {
		log(Log.DEBUG, tag, contents);
	}

	/**
	 * Log.INFO 信息日志
	 *
	 * @param contents 内容
	 */
	@Override
	public void i(Object... contents) {
		iTag(TAG, contents);
	}

	/**
	 * Log.INFO 信息日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	@Override
	public void iTag(String tag, Object... contents) {
		log(Log.INFO, tag, contents);
	}

	/**
	 * Log.WARN 警告日志
	 *
	 * @param contents 内容
	 */
	@Override
	public void w(Object... contents) {
		wTag(TAG, contents);
	}

	/**
	 * Log.WARN 警告日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	@Override
	public void wTag(String tag, Object... contents) {
		log(Log.WARN, tag, contents);
	}

	/**
	 * Log.ERROR 错误日志
	 *
	 * @param contents 内容
	 */
	@Override
	public void e(Object... contents) {
		eTag(TAG, contents);
	}

	/**
	 * Log.ERROR 错误日志
	 *
	 * @param tag      tag
	 * @param contents 内容
	 */
	@Override
	public void eTag(String tag, Object... contents) {
		log(Log.ERROR, tag, contents);
	}

	private synchronized void log(int priority, String tag, Object... contents) {
		StringBuilder sb = new StringBuilder();
		sb.append(getStackInfo());
		String body;
		try {
			if (contents.length == 1) {
				sb.append(contents[0]);
			} else {
				for (int i = 0, len = contents.length; i < len; ++i) {
					Object content = contents[i];
					sb.append("[")
							.append(i)
							.append("]")
							.append(" = ")
							.append(content)
							.append(LINE_SEPARATOR);
				}
			}
		} catch (Exception e) {
			sb.append(e.getMessage());
		} finally {
			sb.append(LINE_SEPARATOR);
			body = sb.toString();
			switch (priority) {
				case Log.INFO:
					LogUtil.i(tag, body);
					break;
				case Log.WARN:
					LogUtil.w(tag, body);
					break;
				case Log.ERROR:
					LogUtil.e(tag, body);
					break;
				case Log.DEBUG:
				case Log.VERBOSE:
				default:
					LogUtil.d(tag, body);
					break;
			}
		}
	}

	/**
	 * 获取堆栈
	 */
	private String getStackInfo() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DOUBLE_DIVIDER);
		stringBuilder.append(LINE_SEPARATOR);
		stringBuilder.append("[Thread] → ").append(Thread.currentThread().getName());
		String str = "";
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();

		for (StackTraceElement element : traces) {
			StringBuilder perTrace = new StringBuilder(str);
			if (element.isNativeMethod()) {
				continue;
			}
			String className = element.getClassName();
			// 过滤包名
			if (className.startsWith("android.")
					|| className.contains("com.android")
					|| className.contains("androidx.")
					|| className.contains("java.lang")
					|| className.contains("com.yyxnb.android.log")
			) {
				continue;
			}
			perTrace.append(element.getClassName())
					.append('.')
					.append(element.getMethodName())
					.append("  (")
					.append(element.getFileName())
					.append(':')
					.append(element.getLineNumber())
					.append(")");
			str += "  ";
			stringBuilder.append(perTrace);
			stringBuilder.append(LINE_SEPARATOR);
		}
		stringBuilder.append(SINGLE_DIVIDER);
		stringBuilder.append(LINE_SEPARATOR);
		return stringBuilder.toString();
	}
}
