package com.yyxnb.simple.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yyxnb.what.core.UITask;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        if (initLayoutResId() > 0) {
            setContentView(initLayoutResId());
        }
        initView(savedInstanceState);
        getWindow().getDecorView().post(() -> UITask.post(this::initViewData));
    }

    protected abstract int initLayoutResId();

    protected abstract void initView(Bundle savedInstanceState);

    protected void initViewData() {
    }
}
