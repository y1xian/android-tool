package com.yyxnb.widget.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yyxnb.view.popup.code.FullScreenPopup;
import com.yyxnb.widget.R;

/**
 * 全屏
 */
public class CustomFullScreenPopup extends FullScreenPopup {

    public CustomFullScreenPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int initLayoutResId() {
        return R.layout.popup_fullscreen_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
    }
}
