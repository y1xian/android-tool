package com.yyxnb.android.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;


import com.yyxnb.android.skin.SkinManager;
import com.yyxnb.android.skin.attr.ISkinType;
import com.yyxnb.android.skin.attr.SkinAttr;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 解析当前View声明的属性列表,转换为SkinAttr对象
     *
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttr(Context context, AttributeSet attrs) {
        List<SkinAttr> skinAttrs = new ArrayList<>();

        // 这里的 count 是声明使用的属性的数量，不是所有的属性的数量
        int attrCount = attrs.getAttributeCount();
        for (int index = 0; index < attrCount; index++) {
            // 获取属性名称
            String attrName = attrs.getAttributeName(index);
            // 获取属性的值,这里需要换肤的属性值为对应资源中的 id
            String attrValue = attrs.getAttributeValue(index);

            // 默认支持background textColor src
            ISkinType skinType = getSkinType();
            if (skinType != null && skinType.isSkinType(attrName)) {
                // 获取资源的名称
                String resName = getResName(context, attrValue);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                // 封装属性
                SkinAttr skinAttr = new SkinAttr(resName, skinType, attrs);
                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }

    /**
     * 获取资源名称
     *
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        // 这里如果是资源中的值则是 @ 开头的id,否则不做处理
        if (attrValue.startsWith("@")) {
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);
            // 返回据资源的Id获取对应资源的名称
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 创建对应的属性类型
     *
     * @return
     */
    private static ISkinType getSkinType() {
        try {
            return SkinManager.getInstance().getSkinTypeClazz().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
