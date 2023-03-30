package com.yyxnb.android.skin;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;


import com.yyxnb.android.skin.attr.ISkinType;
import com.yyxnb.android.skin.attr.SkinAttrHolder;
import com.yyxnb.android.skin.attr.SkinTypeImpl;
import com.yyxnb.android.skin.callback.OnSkinChangeCallback;
import com.yyxnb.android.skin.config.SkinConfig;
import com.yyxnb.android.skin.config.SkinUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 皮肤管理类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinManager {

	private static volatile SkinManager mInstance = null;

	private Context mContext;

	/**
	 * 缓存每个页面的控件属性列表
	 */
	private Map<OnSkinChangeCallback, List<SkinAttrHolder>> mPageSkinAttrHolders;

	private SkinResource mSkinResource;

	/**
	 * 用于反射创建 ISkinType 类型,写在这里用于扩展通用的自定义属性
	 */
	private Class<? extends ISkinType> mSkinTypeClazz;

	private SkinManager() {
	}

	public static SkinManager getInstance() {
		if (mInstance == null) {
			synchronized (SkinManager.class) {
				if (mInstance == null) {
					mInstance = new SkinManager();
				}
			}
		}
		return mInstance;
	}

	public void init(Context context) {
		if (mContext == null) {
			mContext = context.getApplicationContext();
		}
		if (mPageSkinAttrHolders == null) {
			mPageSkinAttrHolders = new HashMap<>();
		}
		if (mSkinTypeClazz == null) {
			mSkinTypeClazz = SkinTypeImpl.class;
		}

		String currSkinPath = SkinUtil.getInstance(mContext).getSkinPath();
		if (!TextUtils.isEmpty(currSkinPath)) {
			// 当前设置了皮肤,启动时需要加载皮肤
			if (checkSkinFileValid(currSkinPath) == SkinConfig.SKIN_EXCHANGE_FILE_VALID) {
				// 有效的皮肤文件
				mSkinResource = new SkinResource(mContext, currSkinPath);
			} else {
				SkinUtil.getInstance(mContext).clearSkinPath();
			}
		}

	}

	/**
	 * 加载皮肤
	 *
	 * @param skinPath
	 * @return
	 */
	public int loadSkin(String skinPath) {

		// 判断当前的皮肤是否是目标皮肤
		String currSkinPath = SkinUtil.getInstance(mContext).getSkinPath();
		if (skinPath.equals(currSkinPath)) {
			return SkinConfig.SKIN_EXCHANGE_NOT_EXCHANGE;
		}

		int fileValid = checkSkinFileValid(skinPath);
		if (fileValid != SkinConfig.SKIN_EXCHANGE_FILE_VALID) {
			return fileValid;
		}

		// 初始化资源管理器
		mSkinResource = new SkinResource(mContext, skinPath);
		// 换肤
		changeSkin();

		// 将当前皮肤的路径缓存
		SkinUtil.getInstance(mContext).updateSkinPath(skinPath);

		return SkinConfig.SKIN_EXCHANGE_SUCCESS;
	}

	/**
	 * 恢复默认皮肤
	 *
	 * @return
	 */
	public int restoreSkin() {
		// 判断当前皮肤是否是默认的皮肤
		String currSkinPath = SkinUtil.getInstance(mContext).getSkinPath();
		if (TextUtils.isEmpty(currSkinPath)) {
			return SkinConfig.SKIN_EXCHANGE_NOT_EXCHANGE;
		}

		// 获取当前手机运行APP的APK路径
		String skinPath = SkinUtil.getInstance(mContext).getCurrApkPath();
		mSkinResource = new SkinResource(mContext, skinPath);
		// 换肤
		changeSkin();
		// 清除缓存的皮肤路径
		SkinUtil.getInstance(mContext).clearSkinPath();

		return SkinConfig.SKIN_EXCHANGE_SUCCESS;
	}

	/**
	 * 换肤
	 */
	private void changeSkin() {
		Set<OnSkinChangeCallback> set = mPageSkinAttrHolders.keySet();
		for (OnSkinChangeCallback callback : set) {
			List<SkinAttrHolder> skinAttrHolders = mPageSkinAttrHolders.get(callback);
			if (skinAttrHolders == null) {
				return;
			}
			for (SkinAttrHolder attrHolder : skinAttrHolders) {
				attrHolder.skin();
				callback.onSkinChange(attrHolder.getView(), mSkinResource);
			}
		}
	}

	/**
	 * 检测皮肤文件是否完整有效
	 *
	 * @param skinPath
	 * @return
	 */
	private int checkSkinFileValid(String skinPath) {
		// 判断皮肤文件是否存在
		File skinFile = new File(skinPath);
		if (!skinFile.exists()) {
			return SkinConfig.SKIN_EXCHANGE_FILE_NOT_EXISTS;
		}

		// 获取皮肤文件的包名用于判断文件是否完整
		String packageName = SkinUtil.getInstance(mContext).getPackageName(skinPath);
		if (TextUtils.isEmpty(packageName)) {
			return SkinConfig.SKIN_EXCHANGE_FILE_DAMAGE;
		}

		return SkinConfig.SKIN_EXCHANGE_FILE_VALID;
	}

	/**
	 * 是否已换肤
	 *
	 * @return
	 */
	public boolean isChangeSkin() {
		String currSkinPath = SkinUtil.getInstance(mContext).getSkinPath();
		if (!TextUtils.isEmpty(currSkinPath)) {
			// 当前设置了皮肤
			return checkSkinFileValid(currSkinPath) == SkinConfig.SKIN_EXCHANGE_FILE_VALID;
		}
		return false;
	}

	/**
	 * 获取skinChangeCallback对应页面的皮肤属性列表
	 *
	 * @param skinChangeCallback
	 * @return
	 */
	public List<SkinAttrHolder> getSkinAttrHolders(OnSkinChangeCallback skinChangeCallback) {
		return mPageSkinAttrHolders.get(skinChangeCallback);
	}

	public void register(OnSkinChangeCallback skinChangeCallback, List<SkinAttrHolder> skinAttrHolders) {
		mPageSkinAttrHolders.put(skinChangeCallback, skinAttrHolders);
	}

	public void remove(OnSkinChangeCallback skinChangeCallback) {
		mPageSkinAttrHolders.remove(skinChangeCallback);
	}

	/**
	 * 检测是否需要换肤
	 *
	 * @param skinChangeCallback
	 * @param view
	 * @param skinAttrHolder
	 */
	public void checkSkin(OnSkinChangeCallback skinChangeCallback, View view, SkinAttrHolder skinAttrHolder) {
		String currSkinPath = SkinUtil.getInstance(mContext).getSkinPath();
		if (!TextUtils.isEmpty(currSkinPath)) {
			// 切换
			skinAttrHolder.skin();
			skinChangeCallback.onSkinChange(view, mSkinResource);
		}
	}

	/**
	 * 获取当前的皮肤资源管理器
	 *
	 * @return
	 */
	public SkinResource getSkinResource() {
		return mSkinResource;
	}

	/**
	 * 这里支持扩展通用的自定义属性
	 *
	 * @param skinTypeClazz
	 */
	public void exchangeSkinType(Class<? extends ISkinType> skinTypeClazz) {
		this.mSkinTypeClazz = skinTypeClazz;
	}

	public Class<? extends ISkinType> getSkinTypeClazz() {
		return mSkinTypeClazz;
	}

}
