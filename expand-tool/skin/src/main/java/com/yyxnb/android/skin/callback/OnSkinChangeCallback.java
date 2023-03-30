package com.yyxnb.android.skin.callback;

import android.view.View;

import com.yyxnb.android.skin.SkinResource;


/**
 * 换肤回调,用于扩展自定义View的属性
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public interface OnSkinChangeCallback {

	/**
	 * 换肤的回调,用于扩展自定义View的换肤功能
	 *
	 * @param view         view
	 * @param skinResource skinResource
	 */
	void onSkinChange(View view, SkinResource skinResource);

}
