package com.yyxnb.android.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.yyxnb.android.ModuleManager;

import java.util.regex.Pattern;

/**
 * 提供匿名的日志打印
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/4
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LogUtil {

	/**
	 * 数字，英文字母（大小写），中文汉字 正则表达式。
	 */
	private static final Pattern M_PATTERN = Pattern.compile("\\d*[a-z|A-Z]*[\u4E00-\u9FA5]*");

	/**
	 * 扰码所需的常量.
	 */
	private static final char STAR = '*';

	/**
	 * 扰码间隔长度.
	 */
	private static final int LEN_CONST = 2;

	/**
	 * 对要打印的日志进行处理
	 *
	 * @param msg 需要匿名化的信息
	 * @return 匿名化后的信息
	 */
	private static String getLogMsg(String msg) {
		StringBuilder retStr = new StringBuilder(512);
		if (!TextUtils.isEmpty(msg)) {
			// 非debug直接匿名
			if (!ModuleManager.config().isDebug()) {
				retStr.append(formatLogWithStar(msg));
			} else {
				retStr.append(msg);
			}
		}
		return retStr.toString();
	}

	/**
	 * DEBUG级别日志输出函数，不匿名化打印
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 */
	public static void d(String tag, String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Log.d(tag, getLogMsg(msg));
	}

	/**
	 * DEBUG级别日志输出函数，不匿名化打印（带异常打印）
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 * @param e   需要输出的异常堆栈,允许为null.
	 */
	public static void d(String tag, String msg, Throwable e) {
		if (TextUtils.isEmpty(msg) && (null == e)) {
			return;
		}
		Log.d(tag, getLogMsg(msg), getNewThrowable(e));
	}

	/**
	 * INFO级别日志输出函数，不匿名化打印
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 */
	public static void i(String tag, String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Log.i(tag, getLogMsg(msg));
	}

	/**
	 * INFO级别日志输出函数，不匿名化打印（含异常打印）
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 * @param e   需要输出的异常堆栈,允许为null.
	 */
	public static void i(String tag, String msg, Throwable e) {
		if (TextUtils.isEmpty(msg) && (null == e)) {
			return;
		}
		Log.i(tag, getLogMsg(msg), getNewThrowable(e));
	}

	/**
	 * 不匿名化warn打印
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 */
	public static void w(String tag, String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Log.w(tag, getLogMsg(msg));
	}

	/**
	 * 不匿名化warn打印
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 * @param e   需要输出的异常堆栈,允许为null.
	 */
	public static void w(String tag, String msg, Throwable e) {
		if (TextUtils.isEmpty(msg) && (null == e)) {
			return;
		}
		Log.w(tag, getLogMsg(msg), getNewThrowable(e));
	}

	/**
	 * ERROR 级别日志输出函数，不匿名化打印
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 */
	public static void e(String tag, String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Log.e(tag, getLogMsg(msg));
	}

	/**
	 * 不匿名化error打印
	 *
	 * @param tag 需输出的tag.
	 * @param msg 需输出的消息,允许为null.
	 * @param e   需要输出的异常堆栈,允许为null.
	 */
	public static void e(String tag, String msg, Throwable e) {
		if (TextUtils.isEmpty(msg) && (null == e)) {
			return;
		}
		Log.e(tag, getLogMsg(msg), getNewThrowable(e));
	}

	/**
	 * 日志匿名化处理，将日志中部分信息用*代替
	 * 日志中的中文/英文字母（大小写）/数字（0-9），将交替使用“*”替换
	 *
	 * @param logStr 需输出的消息,允许为null.
	 * @return String 返回匿名化后的信息
	 */
	private static String formatLogWithStar(String logStr) {
		try {
			if (TextUtils.isEmpty(logStr)) {
				return logStr;
			}
			final int len = logStr.length();
			if (1 == len) {
				return String.valueOf(STAR);
			}
			StringBuilder retStr = new StringBuilder(len);
			char charAt;
			for (int i = 0, k = 1; i < len; i++) {
				charAt = logStr.charAt(i);
				if (M_PATTERN.matcher(String.valueOf(charAt)).matches()) {
					if (k % LEN_CONST == 0) {
						charAt = STAR;
					}
					k++;
				}
				retStr.append(charAt);
			}
			return retStr.toString();
		} catch (Exception ex) {
			return String.valueOf(STAR);
		}
	}

	/**
	 * 获取匿名化后的异常信息
	 *
	 * @param e Throwable
	 * @return Throwable
	 */
	private static Throwable getNewThrowable(Throwable e) {
		if (!ModuleManager.config().isDebug()) {
			return e;
		}
		if (e == null) {
			return null;
		} else {
			ThrowableWrapper retWrapper = new ThrowableWrapper(e);
			retWrapper.setStackTrace(e.getStackTrace());
			retWrapper.setMessage(modifyExceptionMessage(e.getMessage()));
			ThrowableWrapper preWrapper = retWrapper;
			// 递归修改cause的message消息
			for (Throwable currThrowable = e.getCause(); currThrowable != null; currThrowable = currThrowable
					.getCause()) {
				ThrowableWrapper currWrapper = new ThrowableWrapper(currThrowable);
				currWrapper.setStackTrace(currThrowable.getStackTrace());
				currWrapper.setMessage(modifyExceptionMessage(currThrowable.getMessage()));
				preWrapper.setCause(currWrapper);
				preWrapper = currWrapper;
			}
			return retWrapper;
		}
	}

	/**
	 * 格式化异常信息的message
	 *
	 * @param message 需要匿名的信息
	 * @return 返回匿名后的信息
	 */
	private static String modifyExceptionMessage(String message) {
		if (TextUtils.isEmpty(message)) {
			return message;
		} else {
			char[] messageChars = message.toCharArray();
			for (int i = 0; i < messageChars.length; i++) {
				if (i % 2 == 0) {
					messageChars[i] = '*';
				}
			}
			return new String(messageChars);
		}
	}

	/**
	 * 异常包装对象
	 */
	private static class ThrowableWrapper extends Throwable {
		/**
		 * 序列化id
		 */
		private static final long serialVersionUID = 7129050843360571879L;

		/**
		 * 异常消息内容(修改后)
		 */
		private String message;

		/**
		 * 异常原因
		 */
		private Throwable thisCause;

		/**
		 * 包装的Throwable对象
		 */
		private final Throwable ownerThrowable;

		public ThrowableWrapper(Throwable t) {
			this.ownerThrowable = t;
		}

		@Override
		public synchronized Throwable getCause() {
			return thisCause == this ? null : thisCause;
		}

		public void setCause(Throwable cause) {
			this.thisCause = cause;
		}

		@Override
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@NonNull
		@Override
		public String toString() {
			if (ownerThrowable == null) {
				return "";
			}
			String throwableClzName = ownerThrowable.getClass().getName();
			if (message != null) {
				String prefix = throwableClzName + ": ";
				if (message.startsWith(prefix)) {
					return message;
				} else {
					return prefix + message;
				}
			} else {
				return throwableClzName;
			}
		}
	}
}