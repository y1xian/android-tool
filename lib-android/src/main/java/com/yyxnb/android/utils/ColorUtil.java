package com.yyxnb.android.utils;

import android.graphics.Color;

import com.yyxnb.android.Oh;

/**
 * 颜色相关工具
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/9
 */
public class ColorUtil {

	/**
	 * 预防传进来的非 #RRGGBB #AARRGGBB 而是 #ccc等
	 *
	 * @param rgb 3位则补全 ，报错则返回白色
	 */
	public static int parseColor(String rgb) {
		int color = -1;
		try {
			if (rgb.charAt(0) == '#') {
				if (rgb.length() == 4) {
					rgb = rgb + rgb.replace("#", "");
				}
			}
			color = Color.parseColor(rgb);
		} catch (Exception e) {
			Oh.log().e(e, "颜色无法识别");
		}
		return color;
	}
}
