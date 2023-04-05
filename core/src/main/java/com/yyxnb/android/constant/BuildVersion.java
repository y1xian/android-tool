package com.yyxnb.android.constant;

import android.os.Build;

/**
 * Android build 版本
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/21
 */
public class BuildVersion {

	/**
	 * api 23及以上，M Android 6
	 */
	boolean isOver6() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	/**
	 * api 24及以上，N Android 7
	 */
	boolean isOver7() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
	}

	/**
	 * api 26及以上，O Android 8
	 */
	boolean isOver8() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
	}

	/**
	 * api 28及以上，P Android 9
	 */
	boolean isOver9() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
	}

	/**
	 * api 29及以上，Q Android 10
	 */
	boolean isOver10() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
	}

	/**
	 * api 30及以上，R Android 11
	 */
	boolean isOver11() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
	}

	/**
	 * api 31及以上，S Android 12
	 */
	boolean isOver12() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
	}

	/**
	 * api 33及以上，S Android 13
	 */
	public static boolean isOver13() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
	}
}
