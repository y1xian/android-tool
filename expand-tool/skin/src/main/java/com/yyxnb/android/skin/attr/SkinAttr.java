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

	/***
	 * 对应View的属性，如android:background="@color/xxx"的background
	 */
	public String attrName;

	/***
	 * 属性值对应的reference id值，类似R.color.XX
	 */
	public int attrValueRefId;

	/***
	 * 属性值refrence id对应的名称，如R.color.XX，则此值为"XX"
	 */
	public String attrValueRefName;

	/***
	 * 属性值refrence id对应的类型，如R.color.XX，则此值为color
	 */
	public String attrValueTypeName;

	private final ISkinType mSkinType;

	private final AttributeSet mAttrs;

	public SkinAttr(String attrName, int attrValueRefId, String attrValueRefName, String attrValueTypeName, ISkinType skinType, AttributeSet attrs) {
		this.attrName = attrName;
		this.attrValueRefId = attrValueRefId;
		this.attrValueRefName = attrValueRefName;
		this.attrValueTypeName = attrValueTypeName;
		this.mSkinType = skinType;
		this.mAttrs = attrs;
	}

	public void skin(View view, SkinAttr skinAttr) {
		mSkinType.skin(view, skinAttr, mAttrs);
	}

	@Override
	public String toString() {
		return "SkinAttr \n[\nattrName=" + attrName + ", \n"
				+ "attrValueRefId=" + attrValueRefId + ", \n"
				+ "attrValueRefName=" + attrValueRefName + ", \n"
				+ "attrValueTypeName=" + attrValueTypeName
				+ "\n]";
	}
}
