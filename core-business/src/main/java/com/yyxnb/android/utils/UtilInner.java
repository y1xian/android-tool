package com.yyxnb.android.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import androidx.annotation.RestrictTo;

import com.yyxnb.android.ModuleManager;

import java.io.Closeable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 框架内部类，避免工具间耦合
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/7
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class UtilInner {

	static void init(Application app) {
		UtilActivityLifecycleImpl.INSTANCE.init(app);
	}

	static void unInit(Application app) {
		UtilActivityLifecycleImpl.INSTANCE.unInit(app);
	}

	static void preLoad() {
		preLoad(AdaptScreenUtil.getPreLoadRunnable());
	}

	private static void preLoad(final Runnable... runs) {
		for (final Runnable run : runs) {
			ThreadUtil.getCachedPool().execute(run);
		}
	}

	static Application getApp() {
		return AppUtil.getApp();
	}

	static void addOnAppStatusChangedListener(final AppUtil.OnAppStatusChangedListener listener) {
		UtilActivityLifecycleImpl.INSTANCE.addOnAppStatusChangedListener(listener);
	}

	static void removeOnAppStatusChangedListener(final AppUtil.OnAppStatusChangedListener listener) {
		UtilActivityLifecycleImpl.INSTANCE.removeOnAppStatusChangedListener(listener);
	}

	static void addActivityLifecycleCallbacks(final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(callbacks);
	}

	static void removeActivityLifecycleCallbacks(final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(callbacks);
	}

	static void addActivityLifecycleCallbacks(final Activity activity,
											  final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilActivityLifecycleImpl.INSTANCE.addActivityLifecycleCallbacks(activity, callbacks);
	}

	static void removeActivityLifecycleCallbacks(final Activity activity) {
		UtilActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity);
	}

	static void removeActivityLifecycleCallbacks(final Activity activity,
												 final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilActivityLifecycleImpl.INSTANCE.removeActivityLifecycleCallbacks(activity, callbacks);
	}

	static Activity getTopActivity() {
		return UtilActivityLifecycleImpl.INSTANCE.getTopActivity();
	}

	static List<Activity> getActivityList() {
		return UtilActivityLifecycleImpl.INSTANCE.getActivityList();
	}

	static Application getApplicationByReflect() {
		return UtilActivityLifecycleImpl.INSTANCE.getApplicationByReflect();
	}

	static void relaunchApp() {
		AppUtil.relaunchApp();
	}

	static boolean isEmpty(Object obj) {
		return ObjectUtil.isEmpty(obj);
	}

	static boolean isSpace(final String str) {
		return StrUtil.isSpace(str);
	}

	static boolean isBlank(final String str) {
		return StrUtil.isBlank(str);
	}

	static boolean isBlank(final CharSequence str) {
		return StrUtil.isBlank(str);
	}

	static boolean equals(final CharSequence s1, final CharSequence s2) {
		return StrUtil.equals(s1, s2);
	}

	static boolean equalsIgnoreCase(final CharSequence s1, final CharSequence s2) {
		return StrUtil.equalsIgnoreCase(s1, s2);
	}

	static boolean equals(Object obj1, Object obj2) {
		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return NumberUtil.equals((BigDecimal) obj1, (BigDecimal) obj2);
		}
		return Objects.equals(obj1, obj2);
	}

	static String upperFirst(CharSequence str) {
		return StrUtil.upperFirst(str);
	}

	static void runOnUiThread(final Runnable runnable) {
		ThreadUtil.runOnUiThread(runnable);
	}

	static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
		ThreadUtil.runOnUiThreadDelayed(runnable, delayMillis);
	}

	static boolean isAppForeground() {
		return UtilActivityLifecycleImpl.INSTANCE.isAppForeground();
	}

	static String getLauncherActivity(final String pkg) {
		return ActivityUtil.getLauncherActivity(pkg);
	}

	static boolean isActivityAlive(final Activity activity) {
		return ActivityUtil.isActivityAlive(activity);
	}

	static Intent getLaunchAppIntent(final String pkgName) {
		return IntentUtil.getLaunchAppIntent(pkgName);
	}

	static int getNavBarHeight(final Resources resources) {
		return BarUtil.getNavBarHeight(resources);
	}

	static int dp2px(final float dpValue) {
		return DisplayUtil.dp2px(dpValue);
	}

	static int px2dp(final float pxValue) {
		return DisplayUtil.px2dp(pxValue);
	}

	static int sp2px(final float spValue) {
		return DisplayUtil.sp2px(spValue);
	}

	static int px2sp(final float pxValue) {
		return DisplayUtil.px2sp(pxValue);
	}

	static void fixSoftInputLeaks(final Activity activity) {
		KeyboardUtil.fixSoftInputLeaks(activity);
	}

	static void closeSecure(final Closeable... closeables) {
		IOUtil.closeSecure(closeables);
	}

	static void d(Object... contents) {
		ModuleManager.log().d(contents);
	}

	static void i(Object... contents) {
		ModuleManager.log().i(contents);
	}

	static void w(Object... contents) {
		ModuleManager.log().w(contents);
	}

	static void e(Object... contents) {
		ModuleManager.log().e(contents);
	}

}
