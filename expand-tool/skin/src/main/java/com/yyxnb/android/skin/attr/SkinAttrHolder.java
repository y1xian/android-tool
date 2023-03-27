package com.yyxnb.android.skin.attr;

import android.view.View;

import java.util.List;

/**
 * 用于封装View及其对应的换肤属性
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinAttrHolder {

    private View mView;

    /**
     * 当前控件需要换肤的属性列表
     */
    private List<SkinAttr> mSkinAttr;

    public SkinAttrHolder(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttr = skinAttrs;
    }

    public void skin() {
        for (SkinAttr attr : mSkinAttr) {
            attr.skin(mView);
        }
    }

    public View getView() {
        return mView;
    }

}
