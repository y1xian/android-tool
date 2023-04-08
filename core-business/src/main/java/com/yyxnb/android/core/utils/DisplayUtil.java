package com.yyxnb.android.core.utils;

import android.util.DisplayMetrics;

/**
 * dp,sp 和 px 转换的辅助类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/6
 */
public class DisplayUtil {

	/**
	 * px to dp.
	 */
	public static int px2dp(final float pxValue) {
		final float scale = UtilInner.getApp().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dp to px.
	 */
	public static int dp2px(final float dipValue) {
		final float scale = UtilInner.getApp().getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * dp to px.
	 */
	public static int dp2px(final double dipValue) {
		final float scale = UtilInner.getApp().getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5d);
	}

	/**
	 * px to sp.
	 */
	public static int px2sp(final float pxValue) {
		final float fontScale = UtilInner.getApp().getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * sp to px.
	 */
	public static int sp2px(final float spValue) {
		final float fontScale = UtilInner.getApp().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * pt to px.
	 */
	public static int pt2Px(final float ptValue) {
		DisplayMetrics metrics = UtilInner.getApp().getResources().getDisplayMetrics();
		return (int) (ptValue * metrics.xdpi / 72f + 0.5);
	}

	/**
	 * px to pt.
	 */
	public static int px2Pt(final float pxValue) {
		DisplayMetrics metrics = UtilInner.getApp().getResources().getDisplayMetrics();
		return (int) (pxValue * 72 / metrics.xdpi + 0.5);
	}

}
