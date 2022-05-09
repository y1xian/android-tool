package com.yyxnb.lib.andorid.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * view相关
 *
 * @author yyx
 * @date 2022/4/16
 */
public class ViewUtil {

	/**
	 * 设置view margin
	 *
	 * @param v View
	 * @param l 左
	 * @param t 上
	 * @param r 右
	 * @param b 下
	 */
	public static void setMargins(View v, int l, int t, int r, int b) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins(l, t, r, b);
			v.requestLayout();
		}
	}

	/**
	 * 测量View的宽高
	 *
	 * @param view View
	 */
	public static void measureWidthAndHeight(View view) {
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(widthMeasureSpec, heightMeasureSpec);
	}
}
