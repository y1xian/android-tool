package com.yyxnb.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * ScreenUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class ScreenUtil {

	private ScreenUtil() {
	}

	/**
	 * 获取屏幕的宽度（单位：px）
	 *
	 * @return
	 */
	public static int getScreenWidth() {
		WindowManager wm = (WindowManager) UtilInner.getApp().getSystemService(Context.WINDOW_SERVICE);
		if (wm == null) {
			return -1;
		}
		Point point = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			wm.getDefaultDisplay().getRealSize(point);
		} else {
			wm.getDefaultDisplay().getSize(point);
		}
		return point.x;
	}

	/**
	 * 获取屏幕的高度（单位：px）
	 *
	 * @return
	 */
	public static int getScreenHeight() {
		WindowManager wm = (WindowManager) UtilInner.getApp().getSystemService(Context.WINDOW_SERVICE);
		if (wm == null) {
			return -1;
		}
		Point point = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			wm.getDefaultDisplay().getRealSize(point);
		} else {
			wm.getDefaultDisplay().getSize(point);
		}
		return point.y;
	}

	/**
	 * 获取应用屏幕的宽度（单位：px）
	 *
	 * @return
	 */
	public static int getAppScreenWidth() {
		WindowManager wm = (WindowManager) UtilInner.getApp().getSystemService(Context.WINDOW_SERVICE);
		if (wm == null) {
			return -1;
		}
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);
		return point.x;
	}

	/**
	 * 获取应用屏幕的高度（单位：px）
	 *
	 * @return
	 */
	public static int getAppScreenHeight() {
		WindowManager wm = (WindowManager) UtilInner.getApp().getSystemService(Context.WINDOW_SERVICE);
		if (wm == null) {
			return -1;
		}
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);
		return point.y;
	}

	/**
	 * 获取屏幕密度 DPI
	 *
	 * @return
	 */
	public static int getScreenDensityDpi() {
		return Resources.getSystem().getDisplayMetrics().densityDpi;
	}

	/**
	 * 设置屏幕为全屏
	 *
	 * @param activity
	 */
	public static void setFullScreen(@NonNull final Activity activity) {
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 设置屏幕为非全屏
	 *
	 * @param activity
	 */
	public static void setNonFullScreen(@NonNull final Activity activity) {
		activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 切换屏幕为全屏与否状态
	 *
	 * @param activity
	 */
	public static void toggleFullScreen(@NonNull final Activity activity) {
		boolean isFullScreen = isFullScreen(activity);
		Window window = activity.getWindow();
		if (isFullScreen) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	/**
	 * 判断屏幕是否为全屏
	 *
	 * @param activity
	 * @return
	 */
	public static boolean isFullScreen(@NonNull final Activity activity) {
		int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
	}

	/**
	 * 设置屏幕为横屏
	 *
	 * @param activity
	 */
	@SuppressLint("SourceLockedOrientationActivity")
	public static void setLandscape(@NonNull final Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 设置屏幕为竖屏
	 *
	 * @param activity
	 */
	@SuppressLint("SourceLockedOrientationActivity")
	public static void setPortrait(@NonNull final Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 判断是否横屏
	 *
	 * @return
	 */
	public static boolean isLandscape() {
		return UtilInner.getApp().getResources().getConfiguration().orientation
				== Configuration.ORIENTATION_LANDSCAPE;
	}

	/**
	 * 判断是否竖屏
	 *
	 * @return
	 */
	public static boolean isPortrait() {
		return UtilInner.getApp().getResources().getConfiguration().orientation
				== Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * 获取屏幕旋转角度
	 *
	 * @param activity
	 * @return
	 */
	public static int getScreenRotation(@NonNull final Activity activity) {
		int activityRotation;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			activityRotation = activity.getDisplay().getRotation();
		} else {
			activityRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		}
		switch (activityRotation) {
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
			default:
				return 0;
		}
	}

	/**
	 * 判断 Activity 是否反方向旋转了
	 */
	public static boolean isActivityReverse(final Activity activity) {
		// 获取 Activity 旋转的角度
		int activityRotation = getScreenRotation(activity);
		switch (activityRotation) {
			case Surface.ROTATION_180:
			case Surface.ROTATION_270:
				return true;
			case Surface.ROTATION_0:
			case Surface.ROTATION_90:
			default:
				return false;
		}
	}

	/**
	 * 判断是否锁屏
	 *
	 * @return
	 */
	public static boolean isScreenLock() {
		KeyguardManager km =
				(KeyguardManager) UtilInner.getApp().getSystemService(Context.KEYGUARD_SERVICE);
		if (km == null) {
			return false;
		}
		return km.inKeyguardRestrictedInputMode();
	}
}
