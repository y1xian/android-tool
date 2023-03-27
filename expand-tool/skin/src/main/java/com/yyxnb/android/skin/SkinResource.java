package com.yyxnb.android.skin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yyxnb.android.skin.config.SkinUtil;

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

    /**
     * 实际获取本地apk资源的对象
     */
    private Resources mResources;

    /**
     * 本地apk的包名
     */
    private String mPackageName;

    public SkinResource(Context context, String skinPath) {
        try {
            Resources superRes = context.getResources();
            // 创建 AssetManager
            AssetManager asset = AssetManager.class.newInstance();
            // 反射执行 添加本地下载好的皮肤资源的方法
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            method.invoke(asset, skinPath);
            // 创建读取本地的皮肤资源(zip/apk)的Resources对象
            mResources = new Resources(asset, superRes.getDisplayMetrics(), superRes.getConfiguration());

            // 获取皮肤包的包名
            mPackageName = SkinUtil.getInstance(context).getPackageName(skinPath);
        } catch (Exception e) {
            Log.e("SkinResource", "skinPath:" + skinPath + "， " + e.getMessage());
        }
    }

    /**
     * 根据资源名称获取Drawable对象
     *
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mResources.getIdentifier(resName, "drawable", mPackageName);
            if (resId == 0) {
                resId = mResources.getIdentifier(resName, "mipmap", mPackageName);
            }
            return mResources.getDrawable(resId, null);
        } catch (Exception e) {
            Log.e("getDrawableByName", "resName:" + resName + "， " + e.getMessage());
        }
        return null;
    }

    /**
     * 根据资源名称获取Color对象
     *
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName) {
        try {
            int resId = mResources.getIdentifier(resName, "color", mPackageName);
            return mResources.getColorStateList(resId);
        } catch (Exception e) {
            Log.e("getColorByName", "resName:" + resName + "， " + e.getMessage());
        }
        return null;
    }

}
