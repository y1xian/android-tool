package com.yyxnb.widget.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.view.titlebar.TitleBar;
import com.yyxnb.widget.R;


/**
 * 标题栏.
 */
@BindRes(layoutRes = R.layout.fragment_title)
public class TitleFragment extends BaseFragment {

    private TitleBar mTitleBar;

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

//        LogUtils.INSTANCE.e("" + getHasId() + ", " + getSceneId());

        mTitleBar.setBackListener(v -> {
            finish();
        });

    }

}
