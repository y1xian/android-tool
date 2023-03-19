package com.yyxnb.android.utils;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.lifecycle.Lifecycle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UtilsActivityLifecycleImpl
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/7
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
final class UtilActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

	static final UtilActivityLifecycleImpl INSTANCE = new UtilActivityLifecycleImpl();

	private final LinkedList<Activity> mActivityList = new LinkedList<>();
	private final List<AppUtil.OnAppStatusChangedListener> mStatusListeners = new CopyOnWriteArrayList<>();
	private final Map<Activity, List<AppUtil.ActivityLifecycleCallbacks>> mActivityLifecycleCallbacksMap = new ConcurrentHashMap<>();
	private static final Activity STUB = new Activity();

	private final AtomicInteger mForegroundCount = new AtomicInteger(0);
	private final AtomicInteger mConfigCount = new AtomicInteger(0);
	private final AtomicBoolean mIsBackground = new AtomicBoolean(false);

	void init(Application app) {
		app.registerActivityLifecycleCallbacks(this);
	}

	void unInit(Application app) {
		mActivityList.clear();
		app.unregisterActivityLifecycleCallbacks(this);
	}

	Activity getTopActivity() {
		List<Activity> activityList = getActivityList();
		for (Activity activity : activityList) {
			if (!UtilInner.isActivityAlive(activity)) {
				continue;
			}
			return activity;
		}
		return null;
	}

	List<Activity> getActivityList() {
		if (!mActivityList.isEmpty()) {
			return new LinkedList<>(mActivityList);
		}
		List<Activity> reflectActivities = getActivitiesByReflect();
		mActivityList.addAll(reflectActivities);
		return new LinkedList<>(mActivityList);
	}

	void addOnAppStatusChangedListener(final AppUtil.OnAppStatusChangedListener listener) {
		mStatusListeners.add(listener);
	}

	void removeOnAppStatusChangedListener(final AppUtil.OnAppStatusChangedListener listener) {
		mStatusListeners.remove(listener);
	}

	void addActivityLifecycleCallbacks(final AppUtil.ActivityLifecycleCallbacks listener) {
		addActivityLifecycleCallbacks(STUB, listener);
	}

	void addActivityLifecycleCallbacks(final Activity activity,
									   final AppUtil.ActivityLifecycleCallbacks listener) {
		if (activity == null || listener == null) {
			return;
		}
		UtilInner.runOnUiThread(() -> addActivityLifecycleCallbacksInner(activity, listener));
	}

	boolean isAppForeground() {
		return !mIsBackground.get();
	}

	private void addActivityLifecycleCallbacksInner(final Activity activity,
													final AppUtil.ActivityLifecycleCallbacks callbacks) {
		List<AppUtil.ActivityLifecycleCallbacks> callbacksList = mActivityLifecycleCallbacksMap.get(activity);
		if (callbacksList == null) {
			callbacksList = new CopyOnWriteArrayList<>();
			mActivityLifecycleCallbacksMap.put(activity, callbacksList);
		} else {
			if (callbacksList.contains(callbacks)) {
				return;
			}
		}
		callbacksList.add(callbacks);
	}

	void removeActivityLifecycleCallbacks(final AppUtil.ActivityLifecycleCallbacks callbacks) {
		removeActivityLifecycleCallbacks(STUB, callbacks);
	}

	void removeActivityLifecycleCallbacks(final Activity activity) {
		if (activity == null) {
			return;
		}
		UtilInner.runOnUiThread(() -> mActivityLifecycleCallbacksMap.remove(activity));
	}

	void removeActivityLifecycleCallbacks(final Activity activity,
										  final AppUtil.ActivityLifecycleCallbacks callbacks) {
		if (activity == null || callbacks == null) {
			return;
		}
		UtilInner.runOnUiThread(() -> removeActivityLifecycleCallbacksInner(activity, callbacks));
	}

	private void removeActivityLifecycleCallbacksInner(final Activity activity,
													   final AppUtil.ActivityLifecycleCallbacks callbacks) {
		List<AppUtil.ActivityLifecycleCallbacks> callbacksList = mActivityLifecycleCallbacksMap.get(activity);
		if (callbacksList != null && !callbacksList.isEmpty()) {
			callbacksList.remove(callbacks);
		}
	}

	private void consumeActivityLifecycleCallbacks(Activity activity, Lifecycle.Event event) {
		consumeLifecycle(activity, event, mActivityLifecycleCallbacksMap.get(activity));
		consumeLifecycle(activity, event, mActivityLifecycleCallbacksMap.get(STUB));
	}

	private void consumeLifecycle(Activity activity, Lifecycle.Event event, List<AppUtil.ActivityLifecycleCallbacks> listeners) {
		if (listeners == null) {
			return;
		}
		for (AppUtil.ActivityLifecycleCallbacks listener : listeners) {
			listener.onLifecycleChanged(activity, event);
			if (event.equals(Lifecycle.Event.ON_CREATE)) {
				listener.onActivityCreated(activity);
			} else if (event.equals(Lifecycle.Event.ON_START)) {
				listener.onActivityStarted(activity);
			} else if (event.equals(Lifecycle.Event.ON_RESUME)) {
				listener.onActivityResumed(activity);
			} else if (event.equals(Lifecycle.Event.ON_PAUSE)) {
				listener.onActivityPaused(activity);
			} else if (event.equals(Lifecycle.Event.ON_STOP)) {
				listener.onActivityStopped(activity);
			} else if (event.equals(Lifecycle.Event.ON_DESTROY)) {
				listener.onActivityDestroyed(activity);
			}
		}
		if (event.equals(Lifecycle.Event.ON_DESTROY)) {
			mActivityLifecycleCallbacksMap.remove(activity);
		}
	}

	Application getApplicationByReflect() {
		try {
			@SuppressLint("PrivateApi")
			Class<?> activityThread = Class.forName("android.app.ActivityThread");
			Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
			Object app = activityThread.getMethod("getApplication").invoke(thread);
			if (app == null) {
				throw new NullPointerException("u should init first");
			}
			return (Application) app;
		} catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
			UtilInner.e(e);
		}
		throw new NullPointerException("u should init first");
	}

	@Override
	public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
		if (mActivityList.size() == 0) {
			postStatus(activity, true);
		}
//		LanguageUtil.applyLanguage(activity);
		setAnimatorsEnabled();
		setTopActivity(activity);
		consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_CREATE);
	}

	@Override
	public void onActivityStarted(@NonNull Activity activity) {
		if (!mIsBackground.get()) {
			setTopActivity(activity);
		}
		if (mConfigCount.get() < 0) {
			mConfigCount.incrementAndGet();
		} else {
			mForegroundCount.incrementAndGet();
		}
		consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_START);
	}

	@Override
	public void onActivityResumed(@NonNull Activity activity) {
		setTopActivity(activity);
		if (mIsBackground.get()) {
			mIsBackground.set(false);
			postStatus(activity, true);
		}
		processHideSoftInputOnActivityDestroy(activity, false);
		consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_RESUME);
	}

	@Override
	public void onActivityPaused(@NonNull Activity activity) {
		consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_PAUSE);
	}

	@Override
	public void onActivityStopped(@NonNull Activity activity) {
		if (activity.isChangingConfigurations()) {
			mConfigCount.decrementAndGet();
		} else {
			mForegroundCount.decrementAndGet();
			if (mForegroundCount.get() <= 0) {
				mIsBackground.set(true);
				postStatus(activity, false);
			}
		}
		processHideSoftInputOnActivityDestroy(activity, true);
		consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_STOP);
	}

	@Override
	public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
	}

	@Override
	public void onActivityDestroyed(@NonNull Activity activity) {
		mActivityList.remove(activity);
		UtilInner.fixSoftInputLeaks(activity);
		consumeActivityLifecycleCallbacks(activity, Lifecycle.Event.ON_DESTROY);
	}

	// -----------------------------------------------

	private void processHideSoftInputOnActivityDestroy(final Activity activity, boolean isSave) {
		try {
			if (isSave) {
				Window window = activity.getWindow();
				final WindowManager.LayoutParams attrs = window.getAttributes();
				final int softInputMode = attrs.softInputMode;
				window.getDecorView().setTag(-123, softInputMode);
				window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			} else {
				final Object tag = activity.getWindow().getDecorView().getTag(-123);
				if (!(tag instanceof Integer)) {
					return;
				}
				UtilInner.runOnUiThreadDelayed(() -> {
					try {
						Window window = activity.getWindow();
						if (window != null) {
							window.setSoftInputMode(((Integer) tag));
						}
					} catch (Exception ignore) {
					}
				}, 100);
			}
		} catch (Exception ignore) {
		}
	}

	private void postStatus(final Activity activity, final boolean isForeground) {
		if (mStatusListeners.isEmpty()) {
			return;
		}
		for (AppUtil.OnAppStatusChangedListener statusListener : mStatusListeners) {
			if (isForeground) {
				statusListener.onForeground(activity);
			} else {
				statusListener.onBackground(activity);
			}
		}
	}

	private void setTopActivity(final Activity activity) {
		if (mActivityList.contains(activity)) {
			if (!mActivityList.getFirst().equals(activity)) {
				mActivityList.remove(activity);
				mActivityList.addFirst(activity);
			}
		} else {
			mActivityList.addFirst(activity);
		}
	}

	/**
	 * @return the activities which topActivity is first position
	 */
	private List<Activity> getActivitiesByReflect() {
		LinkedList<Activity> list = new LinkedList<>();
		Activity topActivity = null;
		try {
			Object activityThread = getActivityThread();
			if (activityThread == null) {
				return list;
			}
			Field mActivitiesField = activityThread.getClass().getDeclaredField("mActivities");
			mActivitiesField.setAccessible(true);
			Object mActivities = mActivitiesField.get(activityThread);
			if (!(mActivities instanceof Map)) {
				return list;
			}
			Map<Object, Object> binder_activityClientRecord_map = (Map<Object, Object>) mActivities;
			for (Object activityRecord : binder_activityClientRecord_map.values()) {
				Class activityClientRecordClass = activityRecord.getClass();
				Field activityField = activityClientRecordClass.getDeclaredField("activity");
				activityField.setAccessible(true);
				Activity activity = (Activity) activityField.get(activityRecord);
				if (topActivity == null) {
					Field pausedField = activityClientRecordClass.getDeclaredField("paused");
					pausedField.setAccessible(true);
					if (!pausedField.getBoolean(activityRecord)) {
						topActivity = activity;
					} else {
						list.addFirst(activity);
					}
				} else {
					list.addFirst(activity);
				}
			}
		} catch (Exception e) {
			UtilInner.e("UtilsActivityLifecycle", "getActivitiesByReflect: " + e.getMessage());
		}
		if (topActivity != null) {
			list.addFirst(topActivity);
		}
		return list;
	}

	private Object getActivityThread() {
		Object activityThread = getActivityThreadInActivityThreadStaticField();
		if (activityThread != null) {
			return activityThread;
		}
		return getActivityThreadInActivityThreadStaticMethod();
	}

	private Object getActivityThreadInActivityThreadStaticField() {
		try {
			Class activityThreadClass = Class.forName("android.app.ActivityThread");
			Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
			sCurrentActivityThreadField.setAccessible(true);
			return sCurrentActivityThreadField.get(null);
		} catch (Exception e) {
			UtilInner.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticField: " + e.getMessage());
			return null;
		}
	}

	private Object getActivityThreadInActivityThreadStaticMethod() {
		try {
			Class activityThreadClass = Class.forName("android.app.ActivityThread");
			return activityThreadClass.getMethod("currentActivityThread").invoke(null);
		} catch (Exception e) {
			UtilInner.e("UtilsActivityLifecycle", "getActivityThreadInActivityThreadStaticMethod: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Set animators enabled.
	 */
	private static void setAnimatorsEnabled() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ValueAnimator.areAnimatorsEnabled()) {
			return;
		}
		try {
			@SuppressLint("SoonBlockedPrivateApi") Field sDurationScaleField =
					ValueAnimator.class.getDeclaredField("sDurationScale");
			sDurationScaleField.setAccessible(true);
			float sDurationScale = (Float) sDurationScaleField.get(null);
			if (sDurationScale == 0f) {
				sDurationScaleField.set(null, 1f);
				UtilInner.d("UtilsActivityLifecycle", "setAnimatorsEnabled: Animators are enabled now!");
			}
		} catch (NoSuchFieldException e) {
			UtilInner.e(e);
		} catch (IllegalAccessException e) {
			UtilInner.e(e);
		}
	}
}
