package com.yyxnb.android.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LifecycleUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/5/17
 */
public class LifecycleUtil {

	private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
	private static ActivityLifecycleCallbacks lifecycleCallbacks;

	public static void setLifecycleCallbacks(Application application) {
		application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
				LifecycleUtil.onActivityCreated(activity, savedInstanceState);
			}

			@Override
			public void onActivityStarted(@NonNull Activity activity) {
				LifecycleUtil.onActivityStarted(activity);
			}

			@Override
			public void onActivityResumed(@NonNull Activity activity) {
				LifecycleUtil.onActivityResumed(activity);
			}

			@Override
			public void onActivityPaused(@NonNull Activity activity) {
				LifecycleUtil.onActivityPaused(activity);
			}

			@Override
			public void onActivityStopped(@NonNull Activity activity) {
				LifecycleUtil.onActivityStopped(activity);
			}

			@Override
			public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
				LifecycleUtil.onActivitySaveInstanceState(activity, outState);
			}

			@Override
			public void onActivityDestroyed(@NonNull Activity activity) {
				LifecycleUtil.onActivityDestroyed(activity);
			}
		});
	}

	private static void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
		}
	}

	private static void onActivityStarted(Activity activity) {
		if (activity != null) {
			ATOMIC_INTEGER.incrementAndGet();
		}
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivityStarted(activity);
		}
	}

	private static void onActivityResumed(Activity activity) {
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivityResumed(activity);
		}
	}

	private static void onActivityPaused(Activity activity) {
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivityPaused(activity);
		}
	}

	private static void onActivityStopped(Activity activity) {
		if (activity != null) {
			ATOMIC_INTEGER.decrementAndGet();
		}
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivityStopped(activity);
		}
	}

	private static void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
		}
	}

	private static void onActivityDestroyed(Activity activity) {
		if (lifecycleCallbacks != null) {
			lifecycleCallbacks.onActivityDestroyed(activity);
		}
	}

	// -----------------------------------------------------------------------

	public static void setLifecycleCallbacks(ActivityLifecycleCallbacks lifecycleCallbacks) {
		LifecycleUtil.lifecycleCallbacks = lifecycleCallbacks;
	}

	public static ActivityLifecycleCallbacks getLifecycleCallbacks() {
		return lifecycleCallbacks;
	}

	/**
	 * 是否处于前台
	 *
	 * @return {@code true} 前台
	 */
	public static boolean isForeground() {
		return ATOMIC_INTEGER.get() > 0;
	}

	public static AtomicInteger getActivityCount() {
		return ATOMIC_INTEGER;
	}
}
