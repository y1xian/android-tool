package com.yyxnb.lib.andorid.utils;

import android.graphics.Color;

import com.yyxnb.lib.andorid.log.LogUtil;

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
			LogUtil.e(e, "颜色无法识别");
		}
		return color;
	}
}
