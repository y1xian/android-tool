package com.yyxnb.android.core.utils;

import static android.Manifest.permission.VIBRATE;

import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.RequiresPermission;

/**
 * VibrateUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class VibrateUtil {

	private static Vibrator vibrator;

	private VibrateUtil() {
	}

	@RequiresPermission(VIBRATE)
	public static void vibrate(final long milliseconds) {
		Vibrator vibrator = getVibrator();
		if (vibrator == null) {
			return;
		}
		vibrator.vibrate(milliseconds);
	}

	@RequiresPermission(VIBRATE)
	public static void vibrate(final long[] pattern, final int repeat) {
		Vibrator vibrator = getVibrator();
		if (vibrator == null) {
			return;
		}
		vibrator.vibrate(pattern, repeat);
	}

	@RequiresPermission(VIBRATE)
	public static void cancel() {
		Vibrator vibrator = getVibrator();
		if (vibrator == null) {
			return;
		}
		vibrator.cancel();
	}

	private static Vibrator getVibrator() {
		if (vibrator == null) {
			vibrator = (Vibrator) UtilInner.getApp().getSystemService(Context.VIBRATOR_SERVICE);
		}
		return vibrator;
	}
}
