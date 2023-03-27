package com.yyxnb.android.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyxnb.android.skin.SkinManager;
import com.yyxnb.android.skin.SkinResource;

/**
 * 通用的皮肤属性解析,自定义属性解析时可以继承该类,也可以实现 ISkinType 接口
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinType implements ISkinType {

	private ExchangeAttr mExchangeAttr;

	@Override
	public void skin(View view, String resName) {
		if (mExchangeAttr == null) {
			return;
		}
		switch (mExchangeAttr) {
			case TEXT_COLOR:    // 设置文本颜色
				setTextColor(view, resName);
				break;
			case BACKGROUND:    // 设置背景颜色
				setBackground(view, resName);
				break;
			case SRC:   // 设置图片资源
				setSrc(view, resName);
				break;
			default:
				break;
		}
	}

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
		TEXT_COLOR("textColor"),
		BACKGROUND("background"),
		SRC("src");

		private String mAttr;

		ExchangeAttr(String attr) {
			this.mAttr = attr;
		}

	}

	// region ------------ 设置属性 ------------

	/**
	 * 设置图片资源
	 *
	 * @param view
	 * @param resName
	 */
	protected void setSrc(View view, String resName) {
		Drawable drawable = getSkinResource().getDrawableByName(resName);
		if (drawable != null && view instanceof ImageView) {
			Log.d("setSrc", String.format("setImageDrawable1 %s, %s, %s", view, resName, drawable));
			((ImageView) view).setImageDrawable(drawable);
			return;
		}
		ColorStateList color = getSkinResource().getColorByName(resName);
		if (color != null && view instanceof ImageView) {
			Log.d("setSrc", String.format("setImageDrawable2 %s, %s, %s", view, resName, drawable));
			((ImageView) view).setImageDrawable(new ColorDrawable(color.getDefaultColor()));
		}
	}

	/**
	 * 设置背景
	 *
	 * @param view
	 * @param resName
	 */
	protected void setBackground(View view, String resName) {
		Drawable drawable = getSkinResource().getDrawableByName(resName);
		if (drawable != null) {
			Log.d("setBackground", String.format("setBackground %s, %s, %s", view, resName, drawable));
			view.setBackground(drawable);
			return;
		}
		ColorStateList color = getSkinResource().getColorByName(resName);
		if (color != null) {
			Log.d("setBackground", String.format("setBackgroundColor %s, %s, %s", view, resName, color));
			view.setBackgroundColor(color.getDefaultColor());
		}
	}

	/**
	 * 设置文本颜色
	 *
	 * @param view
	 * @param resName
	 */
	protected void setTextColor(View view, String resName) {
		ColorStateList color = getSkinResource().getColorByName(resName);
		if (color != null && view instanceof TextView) {
			Log.d("setTextColor", String.format("setTextColor %s, %s", view, resName));
			((TextView) view).setTextColor(color);
		}
	}

	// endregion ------------------------------------
}
