package com.yyxnb.android.skin.attr;

import android.util.AttributeSet;
import android.view.View;

/**
 * 换肤属性接口,用于扩展通用的自定义属性,参考SkinType
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public interface ISkinType {

	/**
	 * 换肤
	 *
	 * @param view     view
	 * @param skinAttr skinAttr
	 * @param attrs    attrs
	 */
	void skin(View view, SkinAttr skinAttr, AttributeSet attrs);

	/**
	 * 判断是否是需要换肤的属性
	 *
	 * @param attrName View的属性的名称，如android:background="@color/xxx"的background
	 * @return 是否需要换肤
	 */
	boolean isSkinType(String attrName);

}
