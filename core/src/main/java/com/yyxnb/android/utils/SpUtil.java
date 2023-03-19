package com.yyxnb.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SpUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class SpUtil {

	private static final Map<String, SpUtil> SP_UTILS_MAP = new HashMap<>();

	private SharedPreferences sp;

	private SpUtil() {
	}

	public static SpUtil getInstance() {
		return getInstance("", Context.MODE_PRIVATE);
	}

	public static SpUtil getInstance(final int mode) {
		return getInstance("", mode);
	}

	public static SpUtil getInstance(String spName, final int mode) {
		if (UtilInner.isSpace(spName)) {
			spName = "SpUtil";
		}
		SpUtil spUtils = SP_UTILS_MAP.get(spName);
		if (spUtils == null) {
			synchronized (SpUtil.class) {
				spUtils = SP_UTILS_MAP.get(spName);
				if (spUtils == null) {
					spUtils = new SpUtil(spName, mode);
					SP_UTILS_MAP.put(spName, spUtils);
				}
			}
		}
		return spUtils;
	}

	private SpUtil(final String spName) {
		sp = UtilInner.getApp().getSharedPreferences(spName, Context.MODE_PRIVATE);
	}

	private SpUtil(final String spName, final int mode) {
		sp = UtilInner.getApp().getSharedPreferences(spName, mode);
	}

	public void put(@NonNull final String key, final String value) {
		put(key, value, false);
	}

	public void put(@NonNull final String key, final String value, final boolean isCommit) {
		if (isCommit) {
			sp.edit().putString(key, value).commit();
		} else {
			sp.edit().putString(key, value).apply();
		}
	}

	public String getString(@NonNull final String key) {
		return getString(key, "");
	}

	public String getString(@NonNull final String key, final String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public void put(@NonNull final String key, final int value) {
		put(key, value, false);
	}

	public void put(@NonNull final String key, final int value, final boolean isCommit) {
		if (isCommit) {
			sp.edit().putInt(key, value).commit();
		} else {
			sp.edit().putInt(key, value).apply();
		}
	}

	public int getInt(@NonNull final String key) {
		return getInt(key, -1);
	}

	public int getInt(@NonNull final String key, final int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	public void put(@NonNull final String key, final long value) {
		put(key, value, false);
	}

	public void put(@NonNull final String key, final long value, final boolean isCommit) {
		if (isCommit) {
			sp.edit().putLong(key, value).commit();
		} else {
			sp.edit().putLong(key, value).apply();
		}
	}

	public long getLong(@NonNull final String key) {
		return getLong(key, -1L);
	}

	public long getLong(@NonNull final String key, final long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public void put(@NonNull final String key, final float value) {
		put(key, value, false);
	}

	public void put(@NonNull final String key, final float value, final boolean isCommit) {
		if (isCommit) {
			sp.edit().putFloat(key, value).commit();
		} else {
			sp.edit().putFloat(key, value).apply();
		}
	}

	public float getFloat(@NonNull final String key) {
		return getFloat(key, -1f);
	}

	public float getFloat(@NonNull final String key, final float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	public void put(@NonNull final String key, final boolean value) {
		put(key, value, false);
	}

	public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
		if (isCommit) {
			sp.edit().putBoolean(key, value).commit();
		} else {
			sp.edit().putBoolean(key, value).apply();
		}
	}

	public boolean getBoolean(@NonNull final String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	public void put(@NonNull final String key, final Set<String> value) {
		put(key, value, false);
	}

	public void put(@NonNull final String key,
					final Set<String> value,
					final boolean isCommit) {
		if (isCommit) {
			sp.edit().putStringSet(key, value).commit();
		} else {
			sp.edit().putStringSet(key, value).apply();
		}
	}

	public Set<String> getStringSet(@NonNull final String key) {
		return getStringSet(key, Collections.<String>emptySet());
	}

	public Set<String> getStringSet(@NonNull final String key,
									final Set<String> defaultValue) {
		return sp.getStringSet(key, defaultValue);
	}

	public Map<String, ?> getAll() {
		return sp.getAll();
	}

	public boolean contains(@NonNull final String key) {
		return sp.contains(key);
	}

	public void remove(@NonNull final String key) {
		remove(key, false);
	}

	public void remove(@NonNull final String key, final boolean isCommit) {
		if (isCommit) {
			sp.edit().remove(key).commit();
		} else {
			sp.edit().remove(key).apply();
		}
	}

	public void clear() {
		clear(false);
	}

	public void clear(final boolean isCommit) {
		if (isCommit) {
			sp.edit().clear().commit();
		} else {
			sp.edit().clear().apply();
		}
	}
}
