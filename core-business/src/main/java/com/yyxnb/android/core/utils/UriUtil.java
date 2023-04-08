package com.yyxnb.android.core.utils;

import android.net.Uri;

/**
 * UriUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class UriUtil {

	private UriUtil() {
	}

	public static Uri res2Uri(String resPath) {
		return Uri.parse("android.resource://" + UtilInner.getApp().getPackageName() + "/" + resPath);
	}
}
