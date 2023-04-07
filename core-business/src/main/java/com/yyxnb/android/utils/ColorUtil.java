package com.yyxnb.android.utils;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * ColorUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class ColorUtil {

	private ColorUtil() {
	}

	/**
	 * 获取颜色
	 *
	 * @param id
	 * @return
	 */
	public static int getColor(@ColorRes int id) {
		return ContextCompat.getColor(UtilInner.getApp(), id);
	}

	/**
	 * 颜色串转颜色值
	 *
	 * @param colorString
	 * @return
	 */
	public static int string2Int(@NonNull String colorString) {
		return Color.parseColor(colorString);
	}

	/**
	 * 颜色值转 RGB 串
	 *
	 * @param colorInt
	 * @return
	 */
	public static String int2RgbString(@ColorInt int colorInt) {
		colorInt = colorInt & 0x00ffffff;
		StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
		while (color.length() < 6) {
			color.insert(0, "0");
		}
		return "#" + color;
	}

	/**
	 * 颜色值转 ARGB 串
	 *
	 * @param colorInt
	 * @return
	 */
	public static String int2ArgbString(@ColorInt final int colorInt) {
		StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
		while (color.length() < 6) {
			color.insert(0, "0");
		}
		while (color.length() < 8) {
			color.insert(0, "f");
		}
		return "#" + color;
	}

	/**
	 * 获取随机色
	 *
	 * @param supportAlpha
	 * @return
	 */
	public static int getRandomColor(final boolean supportAlpha) {
		int high = supportAlpha ? (int) (Math.random() * 0x100) << 24 : 0xFF000000;
		return high | (int) (Math.random() * 0x1000000);
	}

	/**
	 * 判断是否亮色
	 *
	 * @param color
	 * @return
	 */
	public boolean isLightColor(@ColorInt int color) {
		return 0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color) >= 127.5;
	}
}
