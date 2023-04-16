package com.yyxnb.android.core.utils;

import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

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

	/**
	 * 状态栏字体颜色 6.0
	 *
	 * @param window window
	 * @param dark   是否深色
	 */
	public static void setStatusBarStyle(Window window, boolean dark) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			View decorView = window.getDecorView();
			int systemUi = decorView.getSystemUiVisibility();
			if (dark) {
				systemUi |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
			} else {
				systemUi &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
			}
			decorView.setSystemUiVisibility(systemUi);
		}
	}

	/**
	 * 是否深色
	 *
	 * @param window window
	 * @return true为深色
	 */
	public static boolean isDarkStatusBarStyle(Window window) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return (window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
		}
		return false;
	}

	/**
	 * 开启沉浸式
	 * 通常为 translucent:true,fitsSystemWindows:false
	 *
	 * @param window            window
	 * @param translucent       状态栏是否透明
	 * @param fitsSystemWindows 触发View的padding属性来给系统窗口留出空间
	 */
	public static void setStatusBarTranslucent(Window window, boolean translucent, boolean fitsSystemWindows) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setRenderContentInShortEdgeCutoutAreas(window, translucent);
			View decorView = window.getDecorView();
			if (translucent) {
				decorView.setOnApplyWindowInsetsListener((v, insets) -> {
					WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
					return defaultInsets.replaceSystemWindowInsets(
							defaultInsets.getSystemWindowInsetLeft(),
							//是否撑开
							fitsSystemWindows ? defaultInsets.getSystemWindowInsetTop() : 0,
							defaultInsets.getSystemWindowInsetRight(),
							defaultInsets.getSystemWindowInsetBottom());
				});
			} else {
				decorView.setOnApplyWindowInsetsListener(null);
			}
			ViewCompat.requestApplyInsets(decorView);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (translucent) {
				window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			} else {
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
			ViewCompat.requestApplyInsets(window.getDecorView());
		}
	}

	/**
	 * 安全区域
	 *
	 * @param window     window
	 * @param shortEdges 是否允许内容区域延伸到刘海区
	 */
	public static void setRenderContentInShortEdgeCutoutAreas(Window window, boolean shortEdges) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			WindowManager.LayoutParams layoutParams = window.getAttributes();
			if (shortEdges) {
				// 允许内容区域延伸到刘海区
				layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
			} else {
				// 全屏模式，内容下移，非全屏不受影响
				// LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER 不允许内容延伸进刘海区
				layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
			}
			window.setAttributes(layoutParams);
		}
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
