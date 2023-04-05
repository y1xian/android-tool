package com.yyxnb.android.utils;

import android.content.pm.PackageManager;
import android.util.Log;
import android.util.Pair;

import com.yyxnb.android.constant.PermissionConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * PermissionUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class PermissionUtil {

	private PermissionUtil() {
	}

	public static List<String> getPermissions() {
		return getPermissions(UtilInner.getApp().getPackageName());
	}

	public static List<String> getPermissions(final String packageName) {
		PackageManager pm = UtilInner.getApp().getPackageManager();
		try {
			String[] permissions = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
			if (permissions == null) {
				return Collections.emptyList();
			}
			return Arrays.asList(permissions);
		} catch (PackageManager.NameNotFoundException e) {
			UtilInner.e(e);
			return Collections.emptyList();
		}
	}

	public static boolean isGranted(final String... permissions) {
		Pair<List<String>, List<String>> requestAndDeniedPermissions = getRequestAndDeniedPermissions(permissions);
		List<String> deniedPermissions = requestAndDeniedPermissions.second;
		if (!deniedPermissions.isEmpty()) {
			return false;
		}
		List<String> requestPermissions = requestAndDeniedPermissions.first;
		for (String permission : requestPermissions) {
			if (!isGranted(permission)) {
				return false;
			}
		}
		return true;
	}

	private static Pair<List<String>, List<String>> getRequestAndDeniedPermissions(final String... permissionsParam) {
		List<String> requestPermissions = new ArrayList<>();
		List<String> deniedPermissions = new ArrayList<>();
		List<String> appPermissions = getPermissions();
		for (String param : permissionsParam) {
			boolean isIncludeInManifest = false;
			String[] permissions = PermissionConstants.getPermissions(param);
			for (String permission : permissions) {
				if (appPermissions.contains(permission)) {
					requestPermissions.add(permission);
					isIncludeInManifest = true;
				}
			}
			if (!isIncludeInManifest) {
				deniedPermissions.add(param);
				Log.e("PermissionUtils", "U should add the permission of " + param + " in manifest.");
			}
		}
		return Pair.create(requestPermissions, deniedPermissions);
	}
}
