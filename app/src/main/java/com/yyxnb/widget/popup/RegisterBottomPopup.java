package com.yyxnb.widget.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yyxnb.view.popup.PopupUtils;
import com.yyxnb.view.popup.code.BottomPopup;
import com.yyxnb.widget.R;

/**
 * 注册
 */
public class RegisterBottomPopup extends BottomPopup {

    public RegisterBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int initLayoutResId() {
        return R.layout.popup_register_bottom_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.ivDel).setOnClickListener(v -> dismiss());


    }

    @Override
    protected int getPopupHeight() {
        return (int) (PopupUtils.getWindowHeight(getContext()) * .90f);
    }
}
