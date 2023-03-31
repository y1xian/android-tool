package com.yyxnb.android.encrypt;

import android.util.Log;

import androidx.annotation.RestrictTo;

/**
 * LogUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/1
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LogUtil {

	private static final String TAG = LogUtil.class.getSimpleName();

	public static void v(String tag, String message) {
		Log.v(getTag(tag), message);
	}

	public static void d(String tag, String message) {
		if (BuildConfig.DEBUG) {
			Log.d(getTag(tag), message);
		}
	}

	public static void i(String tag, String message) {
		Log.i(getTag(tag), message);
	}

	public static void w(String tag, String message) {
		Log.w(getTag(tag), message);
	}

	public static void e(String tag, String message) {
		Log.e(getTag(tag), message);
	}

	public static void e(String tag, String message, Throwable e) {
		Log.e(getTag(tag), message, e);
	}

	private static String getTag(String tag) {
		return TAG + tag;
	}
}
