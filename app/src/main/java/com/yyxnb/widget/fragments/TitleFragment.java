package com.yyxnb.widget.fragments;


import android.os.Bundle;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.view.titlebar.TitleBar;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * 标题栏.
 */
public class TitleFragment extends BaseFragment {

    private TitleBar mTitleBar;

    public static TitleFragment newInstance() {

        Bundle args = new Bundle();

        TitleFragment fragment = new TitleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_title;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitleBar = findViewById(R.id.mTitleBar);

        mTitleBar.setBackgroundResource(R.drawable.shape_gradient_bg);
        mTitleBar.showCenterProgress();

//        mTitleBar.setClickListener((v, action) -> {
//            switch (action) {
//                case ACTION_LEFT_BUTTON:
//                    finish();
//                    break;
//                default:
//                    break;
//            }
//        });

        mTitleBar.setBackListener(v -> {
            finish();
        });

    }
}
