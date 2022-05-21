package com.yyxnb.android.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yyxnb.android.Oh;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2022/03/06
 * 描    述：AppUtil
 * ================================================
 */
public class AppUtil {
	private static final String TAG = AppUtil.class.getSimpleName();
	private static Application sApp;

	public static void init(Application app) {
		if (app == null) {
			Log.e(TAG, "app is null.");
			return;
		}
		if (sApp == null) {
			sApp = app;
			return;
		}
		if (sApp.equals(app)) {
			return;
		}
		sApp = app;
	}

	public static Application getApp() {
		if (sApp != null) {
			return sApp;
		}
		sApp = AppGlobals.getApplication();
		if (sApp == null) {
			throw new NullPointerException("application反射获取失败");
		}
		return sApp;
	}

	public static Context getContext() {
		return getApp().getApplicationContext();
	}

	/**
	 * 判断当前应用是否是debug状态
	 */
	public static boolean isDebug() {
		try {
			ApplicationInfo info = getApp().getApplicationInfo();
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			return false;
		}
	}

	// ----------------------------------------------------------------------- 当前app信息

	/**
	 * 获取某个APP的版本号，如果App没安装，则返回"0.0.0"
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return 版本号
	 */
	public static String getAppVersion(Context context, String packageName) {
		String version = "0.0.0";
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			version = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return version;
	}

	/**
	 * 获取版本code
	 * 也可使用 BuildConfig.VERSION_CODE 替换
	 *
	 * @param context 上下文
	 * @return 版本code
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return 1;
	}

	/**
	 * 获取应用程序名称
	 *
	 * @param context 上下文
	 * @return 应用名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (Exception e) {
			Oh.log().e(e, "获取异常");
		}
		return null;
	}

	/**
	 * 获取包名
	 *
	 * @param context 上下文
	 * @return 包名
	 */
	public static String getPackageName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.packageName;
		} catch (Exception e) {
			Oh.log().e(e, "获取异常");
		}
		return null;
	}

	/**
	 * 获取图标 bitmap
	 *
	 * @param context 上下文
	 * @return 应用图标
	 */
	public static Bitmap getBitmap(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
		BitmapDrawable bd = (BitmapDrawable) d;
		return bd.getBitmap();
	}

	// ----------------------------------------------------------------------- 已安装app信息

	/**
	 * 查询手机内非系统应用
	 *
	 * @param context 上下文
	 * @return 手机内非系统应用列表
	 */
	public static List<PackageInfo> getAllApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		//获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			//判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}

	/**
	 * 查询手机内所有支持分享的应用
	 *
	 * @param context 上下文
	 * @return
	 */
	@SuppressLint("WrongConstant")
	public static List<ResolveInfo> getShareApps(Context context) {
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}

	/**
	 * 已知包名和类名启动应用程序
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @param className   类名
	 */
	public static void startApp(Context context, String packageName, String className) {
		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName(packageName, className);
			intent.setComponent(cn);
			context.startActivity(intent);
		} catch (Exception e) {
			Oh.log().e(e, "启动异常");
		}
	}

	/**
	 * 判断某个APP是否安装
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return true为安装
	 */
	public static boolean isAppInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return installed;
	}

	/**
	 * 重启应用
	 *
	 * @param context 上下文
	 */
	public static void restartPackage(Context context) {
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(launchIntent);
		System.exit(0);
	}

	/**
	 * 跳转到Home页
	 *
	 * @param context 上下文
	 */
	public static void goHome(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}
}
