package com.yyxnb.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;

/**
 * AppUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/5
 */
public final class AppUtil {

	@SuppressLint("StaticFieldLeak")
	private volatile static Application sApplication;

	private AppUtil() {
	}

	public synchronized static void init(final Application app) {
		if (app == null) {
			throw new NullPointerException();
		}
		if (sApplication == null) {
			sApplication = app;
			UtilInner.init(sApplication);
			UtilInner.preLoad();
			return;
		}
		if (sApplication.equals(app)) {
			return;
		}
		UtilInner.unInit(sApplication);
		sApplication = app;
		UtilInner.init(sApplication);
	}

	public static Application getApp() {
		if (sApplication != null) {
			return sApplication;
		}
		Application app = UtilInner.getApplicationByReflect();
		init(app);
		if (sApplication == null) {
			throw new NullPointerException("reflect failed.");
		}
		return app;
	}

	public static void registerAppStatusChangedListener(@NonNull final OnAppStatusChangedListener listener) {
		UtilInner.addOnAppStatusChangedListener(listener);
	}

	public static void unregisterAppStatusChangedListener(@NonNull final OnAppStatusChangedListener listener) {
		UtilInner.removeOnAppStatusChangedListener(listener);
	}

	public static String getAppPackageName() {
		return getApp().getPackageName();
	}

	public static String getAppName() {
		return getAppName(getApp().getPackageName());
	}

	public static String getAppName(final String packageName) {
		if (UtilInner.isSpace(packageName)) {
			return "";
		}
		try {
			PackageManager pm = getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? "" : pi.applicationInfo.loadLabel(pm).toString();
		} catch (PackageManager.NameNotFoundException e) {
			UtilInner.e(e);
			return "";
		}
	}

	public static String getAppVersionName() {
		return getAppVersionName(getApp().getPackageName());
	}

	public static String getAppVersionName(final String packageName) {
		if (UtilInner.isSpace(packageName)) {
			return "";
		}
		try {
			PackageManager pm = getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? "" : pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			UtilInner.e(e);
			return "";
		}
	}

	public static int getAppVersionCode() {
		return getAppVersionCode(getApp().getPackageName());
	}

	public static int getAppVersionCode(final String packageName) {
		if (UtilInner.isSpace(packageName)) {
			return -1;
		}
		try {
			PackageManager pm = getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? -1 : pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			UtilInner.e(e);
			return -1;
		}
	}

	/**
	 * 判断应用是否首次安装
	 *
	 * @return
	 */
	public static boolean isFirstInstalled() {
		try {
			PackageInfo pi = getApp().getPackageManager().getPackageInfo(getApp().getPackageName(), 0);
			return pi.firstInstallTime == pi.lastUpdateTime;
		} catch (PackageManager.NameNotFoundException e) {
			UtilInner.e(e);
			return true;
		}
	}

	/**
	 * 判断是否安装/升级过
	 *
	 * @return
	 */
	public static boolean isAppUpgraded() {
		try {
			long firstInstallTime = getApp().getPackageManager().getPackageInfo(getAppPackageName(), 0).firstInstallTime;
			long lastUpdateTime = getApp().getPackageManager().getPackageInfo(getAppPackageName(), 0).lastUpdateTime;
			return firstInstallTime != lastUpdateTime;
		} catch (Exception e) {
			return false;
		}
	}

	// -----------------------------------------------


	public static void relaunchApp() {
		relaunchApp(false);
	}

	public static void relaunchApp(final boolean isKillProcess) {
		Intent intent = UtilInner.getLaunchAppIntent(getApp().getPackageName());
		if (intent == null) {
			return;
		}
		intent.addFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
		);
		getApp().startActivity(intent);
		if (!isKillProcess) {
			return;
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	public interface OnAppStatusChangedListener {
		void onForeground(Activity activity);

		void onBackground(Activity activity);
	}

	public static class ActivityLifecycleCallbacks {

		public void onActivityCreated(@NonNull Activity activity) {/**/}

		public void onActivityStarted(@NonNull Activity activity) {/**/}

		public void onActivityResumed(@NonNull Activity activity) {/**/}

		public void onActivityPaused(@NonNull Activity activity) {/**/}

		public void onActivityStopped(@NonNull Activity activity) {/**/}

		public void onActivityDestroyed(@NonNull Activity activity) {/**/}

		public void onLifecycleChanged(@NonNull Activity activity, Lifecycle.Event event) {/**/}
	}

	public static final class FileProviderAndroidUtil extends FileProvider {
		@Override
		public boolean onCreate() {
			return true;
		}
	}
}
