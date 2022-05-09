package com.yyxnb.lib.andorid.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

/**
 * 这种方式获取全局的Application 是一种拓展思路。
 * <p>
 * 对于组件化项目,不可能把项目实际的Application下沉到Base,而且各个module也不需要知道Application真实名字
 * <p>
 * 这种一次反射就能获取全局Application对象的方式相比于在Application#OnCreate保存一份的方式显示更加通用了
 */
public class AppGlobals {
	private static final String TAG = AppGlobals.class.getSimpleName();
	private static Application sApplication;

	@SuppressLint("PrivateApi")
	public static Application getApplication() {
		if (sApplication == null) {
			try {
				@SuppressLint("PrivateApi")
				Class<?> activityThread = Class.forName("android.app.ActivityThread");
				Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
				Object app = activityThread.getMethod("getApplication").invoke(thread);

				if (app == null) {
					sApplication = (Application) activityThread
							.getMethod("currentApplication")
							.invoke(null, (Object[]) null);
				} else {
					sApplication = (Application) app;
				}

				if (sApplication == null) {
					return null;
				}
			} catch (IllegalAccessException e) {
				Log.e(TAG, e.getMessage());
			} catch (InvocationTargetException e) {
				Log.e(TAG, e.getMessage());
			} catch (NoSuchMethodException e) {
				Log.e(TAG, e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return sApplication;
	}

}
