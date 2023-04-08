package com.yyxnb.android.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * ActivityUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class ActivityUtil {

	private ActivityUtil() {
	}

	public static void addActivityLifecycleCallbacks(@Nullable final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilInner.addActivityLifecycleCallbacks(callbacks);
	}

	public static void addActivityLifecycleCallbacks(@Nullable final Activity activity,
													 @Nullable final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilInner.addActivityLifecycleCallbacks(activity, callbacks);
	}

	public static void removeActivityLifecycleCallbacks(@Nullable final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilInner.removeActivityLifecycleCallbacks(callbacks);
	}

	public static void removeActivityLifecycleCallbacks(@Nullable final Activity activity) {
		UtilInner.removeActivityLifecycleCallbacks(activity);
	}

	public static void removeActivityLifecycleCallbacks(@Nullable final Activity activity,
														final AppUtil.ActivityLifecycleCallbacks callbacks) {
		UtilInner.removeActivityLifecycleCallbacks(activity, callbacks);
	}

	public static String getLauncherActivity() {
		return getLauncherActivity(UtilInner.getApp().getPackageName());
	}

	public static String getLauncherActivity(@NonNull final String pkg) {
		if (UtilInner.isSpace(pkg)) {
			return "";
		}
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(pkg);
		PackageManager pm = UtilInner.getApp().getPackageManager();
		List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
		if (info == null || info.size() == 0) {
			return "";
		}
		return info.get(0).activityInfo.name;
	}

	public static Activity getTopActivity() {
		return UtilInner.getTopActivity();
	}

	public static boolean isActivityAlive(final Activity activity) {
		return activity != null && !activity.isFinishing()
				&& (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed());
	}

	public static void finishActivity(@NonNull final Activity activity) {
		finishActivity(activity, false);
	}

	public static void finishActivity(@NonNull final Activity activity, final boolean isLoadAnim) {
		activity.finish();
		if (!isLoadAnim) {
			activity.overridePendingTransition(0, 0);
		}
	}

	public static void finishAllActivities() {
		finishAllActivities(false);
	}

	public static void finishAllActivities(final boolean isLoadAnim) {
		List<Activity> activityList = UtilInner.getActivityList();
		for (Activity act : activityList) {
			act.finish();
			if (!isLoadAnim) {
				act.overridePendingTransition(0, 0);
			}
		}
	}

	private static Context getTopActivityOrApp() {
		if (UtilInner.isAppForeground()) {
			Activity topActivity = getTopActivity();
			return topActivity == null ? UtilInner.getApp() : topActivity;
		} else {
			return UtilInner.getApp();
		}
	}
}
