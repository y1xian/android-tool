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
public interface BuildVersion {

	/**
	 * api 23及以上，M Android 6
	 */
	boolean IS_OVER_6 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	/**
	 * api 24及以上，N Android 7
	 */
	boolean IS_OVER_7 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
	/**
	 * api 26及以上，O Android 8
	 */
	boolean IS_OVER_8 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
	/**
	 * api 28及以上，P Android 9
	 */
	boolean IS_OVER_9 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
	/**
	 * api 29及以上，Q Android 10
	 */
	boolean IS_OVER_10 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
	/**
	 * api 30及以上，R Android 11
	 */
	boolean IS_OVER_11 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
	/**
	 * api 31及以上，S Android 12
	 */
	boolean IS_OVER_12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
	/**
	 * api 33及以上，S Android 13
	 */
	boolean IS_OVER_13 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
}
