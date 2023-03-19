package com.yyxnb.android.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

/**
 * BarUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class BarUtil {

	private BarUtil() {
	}

	// ----------------------------------------------- StatusBar

	public static int getStatusBarHeight() {
		Resources resources = UtilInner.getApp().getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		return resources.getDimensionPixelSize(resourceId);
	}

	public static void setStatusBarLightMode(@NonNull final Window window,
											 final boolean isLightMode) {
		View decorView = window.getDecorView();
		int vis = decorView.getSystemUiVisibility();
		if (isLightMode) {
			vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		} else {
			vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		}
		decorView.setSystemUiVisibility(vis);
	}

	public static boolean isStatusBarLightMode(@NonNull final Activity activity) {
		return isStatusBarLightMode(activity.getWindow());
	}

	public static boolean isStatusBarLightMode(@NonNull final Window window) {
		View decorView = window.getDecorView();
		int vis = decorView.getSystemUiVisibility();
		return (vis & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
	}

	// ----------------------------------------------- ActionBar

	public static int getActionBarHeight() {
		TypedValue tv = new TypedValue();
		if (UtilInner.getApp().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			return TypedValue.complexToDimensionPixelSize(
					tv.data, UtilInner.getApp().getResources().getDisplayMetrics()
			);
		}
		return 0;
	}


	// ----------------------------------------------- NavBar

	public static int getNavBarHeight() {
		return getNavBarHeight(UtilInner.getApp().getResources());
	}

	public static int getNavBarHeight(@NonNull final Resources resources) {
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId != 0) {
			return resources.getDimensionPixelSize(resourceId);
		} else {
			return 0;
		}
	}
}
