package com.yyxnb.android.skin.attr;

import android.util.AttributeSet;
import android.view.View;

/**
 * SkinAttr
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinAttr {

	/**
	 * 资源属性的名称
	 */
	private final String mResName;

	private final ISkinType mSkinType;

	private final AttributeSet mAttrs;

	public SkinAttr(String resName, ISkinType skinType, AttributeSet attrs) {
		this.mResName = resName;
		this.mSkinType = skinType;
		this.mAttrs = attrs;
	}

	public void skin(View view) {
		mSkinType.skin(view, mResName, mAttrs);
	}

}
