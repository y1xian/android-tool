package com.yyxnb.widget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import com.yyxnb.arch.base.BaseActivity;
import com.yyxnb.view.titlebar.TitleBar;

import org.jetbrains.annotations.Nullable;

public class ButtonActivity extends BaseActivity {

    private TitleBar mTitleBar;
    private Button btnShow;

    @Override
    public int initLayoutResId() {
        return R.layout.activity_button;
    }

    @SuppressLint("ResourceType")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mTitleBar = findViewById(R.id.mTitleBar);
        btnShow = findViewById(R.id.btnShow);
        mTitleBar.setBackListener(v -> finish());

        btnShow.setOnClickListener(v -> {

//            NavGraphBuilder.INSTANCE.getNavController().navigate(474114952);
//            Navigation.findNavController().navigate(474114952);
        });

    }
}
