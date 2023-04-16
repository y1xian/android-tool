package com.yyxnb.android.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.yyxnb.android.skin.SkinManager;
import com.yyxnb.android.skin.attr.ISkinType;
import com.yyxnb.android.skin.attr.SkinAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * SkinAttrSupport
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinAttrSupport {

	private static final String TAG = SkinAttrSupport.class.getSimpleName();

	/**
	 * 解析当前View声明的属性列表,转换为SkinAttr对象
	 *
	 * @param attrs AttributeSet
	 * @return SkinAttr组
	 */
	public static List<SkinAttr> getSkinAttr(Context context, AttributeSet attrs) {
		List<SkinAttr> skinAttrs = new ArrayList<>();
		// 这里的 count 是声明使用的属性的数量，不是所有的属性的数量
		int attrCount = attrs.getAttributeCount();
		for (int index = 0; index < attrCount; index++) {
			// 获取属性名称，如android:background="@color/xxx"的background
			String attrName = attrs.getAttributeName(index);
			// 获取属性的值,这里需要换肤的属性值为对应资源中的 id
			String attrValue = attrs.getAttributeValue(index);
			// attrName=textColor attrValue=@2131492918 view=TextView
			// 这里如果是资源中的值则是 @ 开头的id,否则不做处理
			if (!attrValue.startsWith("@")) {
				continue;
			}
			// 默认支持background textColor src
			ISkinType skinType = getSkinType();
			if (skinType != null && skinType.isSkinType(attrName)) {
				// 拿到id，如attrValue=@2131492918， 去掉@，得到剩下的2131492918
				int resId = -1;
				try {
					resId = Integer.parseInt(attrValue.substring(1));
				} catch (Exception ex) {
					Log.e(TAG, ex.getMessage());
				}
				if (resId == -1) {
					continue;
				}
				// 根据id获取资源的名称，如R.color.XX，则此值为"XX"
				String resName = context.getResources().getResourceEntryName(resId);
				if (TextUtils.isEmpty(resName)) {
					continue;
				}
				// 判断资源名称是否符合配置的前缀值
				String skinPrefix = SkinManager.getInstance().getSkinPrefix();
				if (!TextUtils.isEmpty(skinPrefix) &&
						!resName.toLowerCase(Locale.ROOT).startsWith(skinPrefix.toLowerCase(Locale.ROOT))) {
					continue;
				}
				// 根据id获取资源的类型，如R.color.XX，则此值为"color"
				String attrValueType = context.getResources().getResourceTypeName(resId);
				// 封装属性
				SkinAttr skinAttr = new SkinAttr(attrName, resId, resName, attrValueType, skinType, attrs);
				skinAttrs.add(skinAttr);
			}
		}
		return skinAttrs;
	}

	/**
	 * 创建对应的属性类型
	 *
	 * @return ISkinType
	 */
	private static ISkinType getSkinType() {
		try {
			return SkinManager.getInstance().getSkinTypeClazz().newInstance();
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
		return null;
	}
}
