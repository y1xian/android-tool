package com.yyxnb.android.utils;

import android.content.Context;

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
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * DisplayMetrics类中属性density
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * DisplayMetrics类中属性density
	 */
	public static int dp2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * DisplayMetrics类中属性density
	 */
	public static int dp2px(Context context, double dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5d);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * DisplayMetrics类中属性scaledDensity
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * DisplayMetrics类中属性scaledDensity
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

}
