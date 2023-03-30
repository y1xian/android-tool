package com.yyxnb.android.skin.config;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * SkinUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinUtil {

	private static volatile SkinUtil mInstance;

	private final Context mContext;

	private SkinUtil(Context context) {
		mContext = context.getApplicationContext();
	}

	public static SkinUtil getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SkinUtil.class) {
				if (mInstance == null) {
					mInstance = new SkinUtil(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 缓存皮肤路径
	 *
	 * @param skinPath 皮肤路径
	 */
	public void updateSkinPath(String skinPath) {
		mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
				.edit()
				.putString(SkinConfig.SKIN_PATH_NAME, skinPath)
				.commit();

	}

	/**
	 * 清除缓存的皮肤路径
	 */
	public void clearSkinPath() {
		updateSkinPath("");
	}

	/**
	 * 获取皮肤路径
	 *
	 * @return 皮肤路径
	 */
	public String getSkinPath() {
		return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
				.getString(SkinConfig.SKIN_PATH_NAME, "");

	}

	/**
	 * 获取目标文件的包名
	 *
	 * @param skinPath 皮肤路径
	 * @return 包名
	 */
	public String getPackageName(String skinPath) {
		return mContext.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
	}

	/**
	 * 获取当前安装APP的APK文件路径
	 *
	 * @return 文件路径
	 */
	public String getCurrApkPath() {
		return mContext.getPackageResourcePath();
	}

}
