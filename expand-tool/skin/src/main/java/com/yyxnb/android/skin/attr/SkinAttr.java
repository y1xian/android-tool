package com.yyxnb.android.skin.attr;

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
    private String mResName;

    private ISkinType mSkinType;

    public SkinAttr(String resName, ISkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View view) {
        mSkinType.skin(view, mResName);
    }

}
