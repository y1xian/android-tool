package com.yyxnb.android.core.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

/**
 * VolumeUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class VolumeUtil {

	private VolumeUtil() {
	}

	/**
	 * 获取音量
	 *
	 * @param streamType
	 * @return
	 */
	public static int getVolume(int streamType) {
		AudioManager am = (AudioManager) UtilInner.getApp().getSystemService(Context.AUDIO_SERVICE);
		return am.getStreamVolume(streamType);
	}

	/**
	 * 设置音量
	 *
	 * @param streamType
	 * @param volume
	 * @param flags
	 */
	public static void setVolume(int streamType, int volume, int flags) {
		AudioManager am = (AudioManager) UtilInner.getApp().getSystemService(Context.AUDIO_SERVICE);
		try {
			am.setStreamVolume(streamType, volume, flags);
		} catch (SecurityException ignore) {
		}
	}

	/**
	 * 获取最大音量
	 *
	 * @param streamType
	 * @return
	 */
	public static int getMaxVolume(int streamType) {
		AudioManager am = (AudioManager) UtilInner.getApp().getSystemService(Context.AUDIO_SERVICE);
		return am.getStreamMaxVolume(streamType);
	}

	/**
	 * 获取最小音量
	 *
	 * @param streamType
	 * @return
	 */
	public static int getMinVolume(int streamType) {
		AudioManager am = (AudioManager) UtilInner.getApp().getSystemService(Context.AUDIO_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			return am.getStreamMinVolume(streamType);
		}
		return 0;
	}
}
