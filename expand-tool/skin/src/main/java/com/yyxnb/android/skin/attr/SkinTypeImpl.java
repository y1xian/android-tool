package com.yyxnb.android.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;

import com.yyxnb.android.skin.SkinManager;
import com.yyxnb.android.skin.SkinResource;

/**
 * 通用的皮肤属性解析,自定义属性解析时可以继承该类,也可以实现 {@link ISkinType} 接口
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinTypeImpl implements ISkinType {

	private ExchangeAttr mExchangeAttr;

	@CallSuper
	@Override
	public void skin(View view, SkinAttr skinAttr, AttributeSet attrs) {
		if (mExchangeAttr == null) {
			return;
		}
		switch (mExchangeAttr) {
			case TEXT_COLOR:
				// 设置文本颜色
				setTextColor(view, skinAttr.attrValueRefName);
				break;
			case BACKGROUND:
				// 设置背景颜色
				setBackground(view, skinAttr.attrValueRefName);
				break;
			case SRC:
				// 设置图片资源
				setSrc(view, skinAttr.attrValueRefName);
				break;
			default:
				break;
		}
	}

	@CallSuper
	@Override
	public boolean isSkinType(String attrName) {
		ExchangeAttr[] exchangeAttrs = ExchangeAttr.values();
		for (ExchangeAttr exchangeAttr : exchangeAttrs) {
			if (exchangeAttr.mAttr.equals(attrName)) {
				mExchangeAttr = exchangeAttr;
				return true;
			}
		}
		return false;
	}

	protected SkinResource getSkinResource() {
		return SkinManager.getInstance().getSkinResource();
	}

	enum ExchangeAttr {
		/**
		 * 设置文本颜色
		 */
		TEXT_COLOR("textColor"),
		/**
		 * 设置背景资源
		 */
		BACKGROUND("background"),
		/**
		 * 设置图片资源
		 */
		SRC("src");

		private final String mAttr;

		ExchangeAttr(String attr) {
			this.mAttr = attr;
		}

	}

	// ----------------------------------------------- 设置属性

	/**
	 * 设置图片资源
	 *
	 * @param view    view
	 * @param resName View的属性值ID对应的名称
	 */
	protected void setSrc(View view, String resName) {
		Drawable drawable = getSkinResource().getDrawableByName(resName);
		if (drawable != null && view instanceof ImageView) {
			((ImageView) view).setImageDrawable(drawable);
			return;
		}
		ColorStateList color = getSkinResource().getColorByName(resName);
		if (color != null && view instanceof ImageView) {
			((ImageView) view).setImageDrawable(new ColorDrawable(color.getDefaultColor()));
		}
	}

	/**
	 * 设置背景
	 *
	 * @param view    view
	 * @param resName View的属性值ID对应的名称
	 */
	protected void setBackground(View view, String resName) {
		Drawable drawable = getSkinResource().getDrawableByName(resName);
		if (drawable != null) {
			view.setBackground(drawable);
			return;
		}
		ColorStateList color = getSkinResource().getColorByName(resName);
		if (color != null) {
			view.setBackgroundColor(color.getDefaultColor());
		}
	}

	/**
	 * 设置文本颜色
	 *
	 * @param view    view
	 * @param resName View的属性值ID对应的名称
	 */
	protected void setTextColor(View view, String resName) {
		ColorStateList color = getSkinResource().getColorByName(resName);
		if (color != null && view instanceof TextView) {
			((TextView) view).setTextColor(color);
		}
	}

}
