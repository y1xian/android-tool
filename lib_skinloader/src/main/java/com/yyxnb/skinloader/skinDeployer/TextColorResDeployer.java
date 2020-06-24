package com.yyxnb.skinloader.skinDeployer;

import android.view.View;
import android.widget.TextView;

import com.yyxnb.skinloader.bean.SkinAttr;
import com.yyxnb.skinloader.bean.SkinConfig;
import com.yyxnb.skinloader.skinInterface.ISkinResDeployer;
import com.yyxnb.skinloader.skinInterface.ISkinResourceManager;


public class TextColorResDeployer implements ISkinResDeployer {
    @Override
    public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
        if (view instanceof TextView && SkinConfig.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            TextView tv = (TextView) view;
            tv.setTextColor(resource.getColorStateList(skinAttr.attrValueRefId));
        }
    }
}
