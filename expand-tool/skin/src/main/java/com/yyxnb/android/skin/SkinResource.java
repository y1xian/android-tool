package com.yyxnb.android.skin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.yyxnb.android.skin.config.SkinConfig;
import com.yyxnb.android.skin.config.SkinUtil;
import com.yyxnb.android.utils.LogUtil;

import java.lang.reflect.Method;

/**
 * 皮肤资源加载类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinResource {

	private static final String TAG = SkinResource.class.getSimpleName();

	/**
	 * 实际获取本地皮肤apk资源的对象
	 */
	private Resources mSkinResources;

	/**
	 * 当前资源对象
	 */
	private Resources mResources;

	/**
	 * 本地apk的包名
	 */
	private String mPackageName;

	@SuppressWarnings("deprecation")
	public SkinResource(Context context, String skinPath) {
		try {
			mResources = context.getResources();
			if (TextUtils.isEmpty(skinPath)) {
				mSkinResources = null;
				mPackageName = context.getPackageName();
				return;
			}
			// 创建 AssetManager
			AssetManager asset = AssetManager.class.newInstance();
			// 反射执行 添加本地下载好的皮肤资源的方法
			Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
			method.setAccessible(true);
			method.invoke(asset, skinPath);
			// 创建读取本地的皮肤资源(zip/apk)的Resources对象
			mSkinResources = new Resources(asset, mResources.getDisplayMetrics(), mResources.getConfiguration());
			// 获取皮肤包的包名
			mPackageName = SkinUtil.getInstance(context).getPackageName(skinPath);
		} catch (Exception e) {
			LogUtil.e(TAG, "skinPath:" + skinPath, e);
		}
	}

	/**
	 * 根据资源名称获取Drawable对象
	 *
	 * @param resName 属性值refrence id对应的名称，如R.color.XX，则此值为"XX"
	 * @return Drawable
	 */
	public Drawable getDrawableByName(String resName) {
		try {
			Resources originResources = mSkinResources;
			if (originResources == null) {
				originResources = mResources;
			}
			int resId = originResources.getIdentifier(resName, SkinConfig.RES_TYPE_NAME_DRAWABLE, mPackageName);
			if (resId == 0) {
				resId = originResources.getIdentifier(resName, SkinConfig.RES_TYPE_NAME_MIPMAP, mPackageName);
			}
			return originResources.getDrawable(resId, null);
		} catch (Exception e) {
			LogUtil.e(TAG, "resName:" + resName, e);
		}
		return null;
	}

	/**
	 * 根据资源名称获取Drawable对象
	 *
	 * @param resId 如R.drawable.XX
	 * @return Drawable
	 */
	public Drawable getDrawableById(int resId) {
		try {
			String resName = mResources.getResourceEntryName(resId);
			return getDrawableByName(resName);
		} catch (Exception e) {
			LogUtil.e(TAG, "resId:" + resId, e);
		}
		return null;
	}

	/**
	 * 根据资源名称获取Color对象
	 *
	 * @param resName 属性值refrence id对应的名称，如R.color.XX，则此值为"XX"
	 * @return ColorStateList
	 */
	public ColorStateList getColorByName(String resName) {
		try {
			Resources originResources = mSkinResources;
			if (originResources == null) {
				originResources = mResources;
			}
			int resId = originResources.getIdentifier(resName, SkinConfig.RES_TYPE_NAME_COLOR, mPackageName);
			return originResources.getColorStateList(resId);
		} catch (Exception e) {
			LogUtil.e(TAG, "resName:" + resName, e);
		}
		return null;
	}

	/**
	 * 根据资源id获取Color对象
	 *
	 * @param resId 如R.color.XX
	 * @return ColorStateList
	 */
	public ColorStateList getColorById(int resId) {
		try {
			String resName = mResources.getResourceEntryName(resId);
			return getColorByName(resName);
		} catch (Exception e) {
			LogUtil.e(TAG, "resId:" + resId, e);
		}
		return null;
	}

}
