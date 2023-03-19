package com.yyxnb.android.utils;

import android.os.Build;

/**
 * DeviceUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class DeviceUtil {

	private DeviceUtil() {
	}

	public static String getManufacturer() {
		return Build.MANUFACTURER;
	}

	public static String getBrand() {
		return Build.BRAND;
	}

	public static String getModel() {
		String model = Build.MODEL;
		if (model != null) {
			model = model.trim().replaceAll("\\s*", "");
		} else {
			model = "";
		}
		return model;
	}

}
