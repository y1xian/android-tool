package com.yyxnb.android.skin.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.view.ViewCompat;

import com.yyxnb.android.skin.SkinManager;
import com.yyxnb.android.skin.attr.SkinAttrHolder;
import com.yyxnb.android.skin.callback.OnSkinChangeCallback;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * SkinCompat
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinCompat {

	// 复制系统的拦截View兼容的对象
	private SkinCompatViewInflater mAppCompatViewInflater;

	private Context mContext;

	private Window mWindow;

	public SkinCompat(Context context, Window window) {
		this.mContext = context;
		this.mWindow = window;
	}

	public void release(OnSkinChangeCallback callback) {
		release();
		if (callback != null) {
			SkinManager.getInstance().remove(callback);
		}
	}

	public void release() {
		mContext = null;
		mWindow = null;
	}

	// region ----------- AppCompatDelegateImpl -----------

	@SuppressLint("RestrictedApi")
	public View createView(View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
		if (this.mAppCompatViewInflater == null) {

			TypedArray a = mContext.obtainStyledAttributes(androidx.appcompat.R.styleable.AppCompatTheme);
			String viewInflaterClassName = a.getString(androidx.appcompat.R.styleable.AppCompatTheme_viewInflaterClass);
			if (viewInflaterClassName != null && !AppCompatViewInflater.class.getName().equals(viewInflaterClassName)) {
				try {
					Class viewInflaterClass = Class.forName(viewInflaterClassName);
					this.mAppCompatViewInflater = (SkinCompatViewInflater) viewInflaterClass.getDeclaredConstructor().newInstance();
				} catch (Throwable var8) {
					Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + viewInflaterClassName + ". Falling back to default.", var8);
					this.mAppCompatViewInflater = new SkinCompatViewInflater();
				}
			} else {
				this.mAppCompatViewInflater = new SkinCompatViewInflater();
			}
		}

		boolean inheritContext = false;
		if (Build.VERSION.SDK_INT < 21) {
			inheritContext = attrs instanceof XmlPullParser ? ((XmlPullParser) attrs).getDepth() > 1 : this.shouldInheritContext((ViewParent) parent);
		}

		return this.mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext, Build.VERSION.SDK_INT < 21, true, VectorEnabledTintResources.shouldBeUsed());
	}

	private boolean shouldInheritContext(ViewParent parent) {
		if (parent == null) {
			return false;
		} else {
			for (View windowDecor = mWindow.getDecorView(); parent != null; parent = parent.getParent()) {
				if (parent == windowDecor || !(parent instanceof View) || ViewCompat.isAttachedToWindow((View) parent)) {
					return false;
				}
			}

			return true;
		}
	}

	// endregion ---------------------------------

	/**
	 * 管理SkinView
	 *
	 * @param callback
	 * @param skinAttrHolder
	 */
	public void managerSkinView(OnSkinChangeCallback callback, SkinAttrHolder skinAttrHolder) {
		// 获取对callback页面的SkinConvert列表
		List<SkinAttrHolder> skinAttrHolders = SkinManager.getInstance().getSkinAttrHolders(callback);
		if (skinAttrHolders == null) {
			skinAttrHolders = new ArrayList<>();
			// 注册
			SkinManager.getInstance().register(callback, skinAttrHolders);
		}
		skinAttrHolders.add(skinAttrHolder);
	}

}
