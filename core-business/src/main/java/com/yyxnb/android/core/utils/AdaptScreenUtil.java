package com.yyxnb.android.core.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * AdaptScreenUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/21
 */
public class AdaptScreenUtil {

	private static List<Field> sMetricsFields;

	private AdaptScreenUtil() {
	}

	@NonNull
	public static Resources adaptWidth(@NonNull final Resources resources, final int designWidth) {
		float newXdpi = (resources.getDisplayMetrics().widthPixels * 72f) / designWidth;
		applyDisplayMetrics(resources, newXdpi);
		return resources;
	}

	@NonNull
	public static Resources adaptHeight(@NonNull final Resources resources, final int designHeight) {
		return adaptHeight(resources, designHeight, false);
	}

	@NonNull
	public static Resources adaptHeight(@NonNull final Resources resources, final int designHeight,
										final boolean includeNavBar) {
		float screenHeight = (resources.getDisplayMetrics().heightPixels
				+ (includeNavBar ? UtilInner.getNavBarHeight(resources) : 0)) * 72f;
		float newXdpi = screenHeight / designHeight;
		applyDisplayMetrics(resources, newXdpi);
		return resources;
	}

	@NonNull
	public static Resources closeAdapt(@NonNull final Resources resources) {
		float newXdpi = Resources.getSystem().getDisplayMetrics().density * 72f;
		applyDisplayMetrics(resources, newXdpi);
		return resources;
	}

	private static void applyDisplayMetrics(@NonNull final Resources resources, final float newXdpi) {
		resources.getDisplayMetrics().xdpi = newXdpi;
		UtilInner.getApp().getResources().getDisplayMetrics().xdpi = newXdpi;
		applyOtherDisplayMetrics(resources, newXdpi);
	}

	static Runnable getPreLoadRunnable() {
		return AdaptScreenUtil::preLoad;
	}

	private static void preLoad() {
		applyDisplayMetrics(Resources.getSystem(), Resources.getSystem().getDisplayMetrics().xdpi);
	}

	private static void applyOtherDisplayMetrics(final Resources resources, final float newXdpi) {
		if (sMetricsFields == null) {
			sMetricsFields = new ArrayList<>();
			Class<?> resCls = resources.getClass();
			Field[] declaredFields = resCls.getDeclaredFields();
			while (!UtilInner.isEmpty(declaredFields) && declaredFields.length > 0) {
				for (Field field : declaredFields) {
					if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
						field.setAccessible(true);
						DisplayMetrics tmpDm = getMetricsFromField(resources, field);
						if (tmpDm != null) {
							sMetricsFields.add(field);
							tmpDm.xdpi = newXdpi;
						}
					}
				}
				resCls = resCls.getSuperclass();
				if (resCls != null) {
					declaredFields = resCls.getDeclaredFields();
				} else {
					break;
				}
			}
		} else {
			applyMetricsFields(resources, newXdpi);
		}
	}

	private static void applyMetricsFields(final Resources resources, final float newXdpi) {
		for (Field metricsField : sMetricsFields) {
			try {
				DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
				if (dm != null) {
					dm.xdpi = newXdpi;
				}
			} catch (Exception e) {
				UtilInner.e(e);
			}
		}
	}

	private static DisplayMetrics getMetricsFromField(final Resources resources, final Field field) {
		try {
			return (DisplayMetrics) field.get(resources);
		} catch (Exception ignore) {
			return null;
		}
	}
}
