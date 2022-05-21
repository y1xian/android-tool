package com.yyxnb.android.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.yyxnb.android.Oh;

/**
 * 亮度相关
 * {@link #isAutoBrightnessEnabled} : 判断是否开启自动调节亮度
 * {@link #setAutoBrightnessEnabled}: 设置是否开启自动调节亮度
 * {@link #getBrightness}           : 获取屏幕亮度
 * {@link #setBrightness}           : 设置屏幕亮度
 * {@link #setWindowBrightness}     : 设置窗口亮度
 * {@link #getWindowBrightness}     : 获取窗口亮度
 *
 * @author yyx
 */
public final class BrightnessUtil {

	private BrightnessUtil() {
	}

	/**
	 * 判断是否开启自动调节亮度.
	 *
	 * @return {@code true}: yes<br>{@code false}: no
	 */
	public static boolean isAutoBrightnessEnabled(Context context) {
		try {
			int mode = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE
			);
			return mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (Settings.SettingNotFoundException e) {
			Oh.log().e(e.getMessage());
			return false;
		}
	}

	/**
	 * 设置是否开启自动调节亮度.
	 * <p>Must hold {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
	 *
	 * @param enabled True to enabled, false otherwise.
	 * @return {@code true}: success<br>{@code false}: fail
	 */
	public static boolean setAutoBrightnessEnabled(Context context, boolean enabled) {
		return Settings.System.putInt(
				context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
						: Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
		);
	}

	/**
	 * 获取屏幕亮度
	 *
	 * @return 屏幕亮度 0-255
	 */
	public static int getBrightness(Context context) {
		try {
			return Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS
			);
		} catch (Settings.SettingNotFoundException e) {
			Oh.log().e(e.getMessage());
			return 0;
		}
	}

	/**
	 * 设置屏幕亮度
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
	 * 并得到授权
	 *
	 * @param brightness 亮度值
	 */
	public static boolean setBrightness(Context context, @IntRange(from = 0, to = 255) int brightness) {
		ContentResolver resolver = context.getContentResolver();
		boolean b = Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
		resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null);
		return b;
	}

	/**
	 * 设置窗口亮度
	 *
	 * @param window     窗口
	 * @param brightness 亮度值
	 */
	public static void setWindowBrightness(@NonNull Window window,
										   @IntRange(from = 0, to = 255) final int brightness) {
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightness / 255f;
		window.setAttributes(lp);
	}

	/**
	 * 获取窗口亮度
	 *
	 * @param window 窗口
	 * @return 屏幕亮度 0-255
	 */
	public static int getWindowBrightness(Context context, Window window) {
		WindowManager.LayoutParams lp = window.getAttributes();
		float brightness = lp.screenBrightness;
		if (brightness < 0) {
			return getBrightness(context);
		}
		return (int) (brightness * 255);
	}
}