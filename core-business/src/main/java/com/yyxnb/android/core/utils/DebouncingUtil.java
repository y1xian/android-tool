package com.yyxnb.android.core.utils;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DebouncingUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/13
 */
public class DebouncingUtil {
	private static final int CACHE_SIZE = 64;
	private static final Map<String, Long> KEY_MILLIS_MAP = new ConcurrentHashMap<>(CACHE_SIZE);
	private static final long DEBOUNCING_DEFAULT_VALUE = 1000;

	private DebouncingUtil() {
	}

	public static boolean isValid(@NonNull final View view) {
		return isValid(view, DEBOUNCING_DEFAULT_VALUE);
	}

	public static boolean isValid(@NonNull final String key) {
		return isValid(key, DEBOUNCING_DEFAULT_VALUE);
	}

	public static boolean isValid(@NonNull final View view, final long duration) {
		return isValid(String.valueOf(view.hashCode()), duration);
	}

	public static boolean isValid(@NonNull String key, final long duration) {
		if (TextUtils.isEmpty(key)) {
			throw new IllegalArgumentException("The key is null.");
		}
		if (duration < 0) {
			throw new IllegalArgumentException("The duration is less than 0.");
		}
		long curTime = SystemClock.elapsedRealtime();
		clear(curTime);
		Long validTime = KEY_MILLIS_MAP.get(key);
		if (validTime == null || curTime >= validTime) {
			KEY_MILLIS_MAP.put(key, curTime + duration);
			return true;
		}
		return false;
	}

	private static void clear(long curTime) {
		if (KEY_MILLIS_MAP.size() < CACHE_SIZE) {
			return;
		}
		for (Iterator<Map.Entry<String, Long>> it = KEY_MILLIS_MAP.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, Long> entry = it.next();
			Long validTime = entry.getValue();
			if (curTime >= validTime) {
				it.remove();
			}
		}
	}
}
