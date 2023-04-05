package com.yyxnb.android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ServiceUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/13
 */
public class ServiceUtil {

	private ServiceUtil() {
	}

	public static Set<String> getAllRunningServices() {
		ActivityManager am = (ActivityManager) UtilInner.getApp().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> info = am.getRunningServices(0x7FFFFFFF);
		Set<String> names = new HashSet<>();
		if (info == null || info.size() == 0) {
			return null;
		}
		for (ActivityManager.RunningServiceInfo aInfo : info) {
			names.add(aInfo.service.getClassName());
		}
		return names;
	}

	public static void startService(@NonNull final String className) {
		try {
			startService(Class.forName(className));
		} catch (Exception e) {
			UtilInner.e(e);
		}
	}

	public static void startService(@NonNull final Class<?> cls) {
		startService(new Intent(UtilInner.getApp(), cls));
	}

	public static void startService(Intent intent) {
		try {
			intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				UtilInner.getApp().startForegroundService(intent);
			} else {
				UtilInner.getApp().startService(intent);
			}
		} catch (Exception e) {
			UtilInner.e(e);
		}
	}

	public static boolean stopService(@NonNull final String className) {
		try {
			return stopService(Class.forName(className));
		} catch (Exception e) {
			UtilInner.e(e);
			return false;
		}
	}

	public static boolean stopService(@NonNull final Class<?> cls) {
		return stopService(new Intent(UtilInner.getApp(), cls));
	}

	public static boolean stopService(@NonNull Intent intent) {
		try {
			return UtilInner.getApp().stopService(intent);
		} catch (Exception e) {
			UtilInner.e(e);
			return false;
		}
	}

	public static void bindService(@NonNull final String className,
								   @NonNull final ServiceConnection conn,
								   final int flags) {
		try {
			bindService(Class.forName(className), conn, flags);
		} catch (Exception e) {
			UtilInner.e(e);
		}
	}

	public static void bindService(@NonNull final Class<?> cls,
								   @NonNull final ServiceConnection conn,
								   final int flags) {
		bindService(new Intent(UtilInner.getApp(), cls), conn, flags);
	}

	public static void bindService(@NonNull final Intent intent,
								   @NonNull final ServiceConnection conn,
								   final int flags) {
		try {
			UtilInner.getApp().bindService(intent, conn, flags);
		} catch (Exception e) {
			UtilInner.e(e);
		}
	}

	public static void unbindService(@NonNull final ServiceConnection conn) {
		UtilInner.getApp().unbindService(conn);
	}

	public static boolean isServiceRunning(@NonNull final Class<?> cls) {
		return isServiceRunning(cls.getName());
	}

	public static boolean isServiceRunning(@NonNull final String className) {
		try {
			ActivityManager am = (ActivityManager) UtilInner.getApp().getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningServiceInfo> info = am.getRunningServices(0x7FFFFFFF);
			if (info == null || info.size() == 0) {
				return false;
			}
			for (ActivityManager.RunningServiceInfo aInfo : info) {
				if (className.equals(aInfo.service.getClassName())) {
					return true;
				}
			}
			return false;
		} catch (Exception ignore) {
			return false;
		}
	}
}
