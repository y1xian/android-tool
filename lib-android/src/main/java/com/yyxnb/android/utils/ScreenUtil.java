package com.yyxnb.android.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.yyxnb.android.Oh;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 屏幕相关
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/8
 */
public class ScreenUtil {

	/**
	 * 获取屏幕像素密度
	 *
	 * @param context 上下文
	 * @return 屏幕像素密度
	 */
	public static float getDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.density;
	}

	/**
	 * 获取scaledDensity
	 *
	 * @param context 上下文
	 * @return
	 */
	public static float getScaledDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.scaledDensity;
	}

	/**
	 * 获取当前的屏幕尺寸
	 *
	 * @param context {@link Context}
	 * @return 屏幕尺寸
	 */
	public static int[] getScreenSize(Context context) {
		int[] size = new int[2];

		WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);

		size[0] = metrics.widthPixels;
		size[1] = metrics.heightPixels;
		return size;
	}

	/**
	 * 获取原始的屏幕尺寸
	 *
	 * @param context {@link Context}
	 * @return 屏幕尺寸
	 */
	public static int[] getRawScreenSize(Context context) {
		int[] size = new int[2];

		WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;

		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
				widthPixels = realSize.x;
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}
		}
		size[0] = widthPixels;
		size[1] = heightPixels;
		return size;
	}

	/**
	 * 获取屏幕的宽度
	 *
	 * @return 屏幕的宽度
	 */
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 获取屏幕的高度
	 *
	 * @return 屏幕的高度
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @param context Context
	 * @return 屏幕宽度（像素）
	 */
	public static int getScreenSizeWidth(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}

	/**
	 * 获得屏幕高度
	 *
	 * @param context Context
	 * @return 屏幕高度（像素）
	 */
	public static int getScreenSizeHeight(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}

	/**
	 * 获取通知栏高度
	 *
	 * @param context 上下文
	 * @return 通知栏高度
	 */
	public static int getStatusBarHeight(Context context) {
		int x = 0, statusBarHeight = 0;
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			Oh.log().e(e1.getMessage());
		}
		return statusBarHeight;
	}

	/**
	 * 获取标题栏高度
	 *
	 * @param context 上下文
	 * @return 标题栏高度
	 */
	public static int getTitleBarHeight(Activity context) {
		int contentTop = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		return contentTop - getStatusBarHeight(context);
	}

	/**
	 * 获取虚拟按键的高度
	 */
	public static int getNavigationBarHeight(Context context) {
		int result = 0;
		if (hasNavBar(context)) {
			Resources res = context.getResources();
			int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = res.getDimensionPixelSize(resourceId);
			}
		}
		return result;
	}

	/**
	 * 检查是否存在虚拟按键栏
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static boolean hasNavBar(Context context) {
		Resources res = context.getResources();
		int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
		if (resourceId != 0) {
			boolean hasNav = res.getBoolean(resourceId);
			String sNavBarOverride = getNavBarOverride();
			if ("1".equals(sNavBarOverride)) {
				hasNav = false;
			} else if ("0".equals(sNavBarOverride)) {
				hasNav = true;
			}
			return hasNav;
		} else {
			return !ViewConfiguration.get(context).hasPermanentMenuKey();
		}
	}

	public static boolean isNavigationBarShow(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			Point realSize = new Point();
			display.getSize(size);
			display.getRealSize(realSize);
			return realSize.y != size.y;
		} else {
			boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
			boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
			return !menu && !back;
		}
	}

	/**
	 * 判断虚拟按键栏是否重写
	 */
	private static String getNavBarOverride() {
		String sNavBarOverride = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method m = c.getDeclaredMethod("get", String.class);
				m.setAccessible(true);
				sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
			} catch (Exception e) {
				Oh.log().e(e.getMessage());
			}
		}
		return sNavBarOverride;
	}

	/**
	 * 屏幕是否铺满
	 *
	 * @param activity activity.
	 * @return {@code true}: yes<br>{@code false}: no
	 */
	public static boolean isFullScreen(@NonNull final Activity activity) {
		int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
	}

	/**
	 * 屏幕旋转角度
	 *
	 * @param activity activity.
	 * @return 角度
	 */
	public static int getScreenRotation(@NonNull final Activity activity) {
		switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
			case Surface.ROTATION_0:
				return 0;
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
}
