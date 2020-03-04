package com.yyxnb.widget.fragments;


import android.os.Bundle;

import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.arch.annotations.BindViewModel;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.common.Message;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.view.titlebar.TitleBar;
import com.yyxnb.widget.R;
import com.yyxnb.widget.vm.MsgViewModel;
import com.yyxnb.widget.vm.NetWorkViewModel;

import org.jetbrains.annotations.Nullable;

/**
 * 标题栏.
 */
@BindFragment(layoutRes = R.layout.fragment_title)
public class TitleFragment extends BaseFragment {

    @BindViewModel(isActivity = true)
    NetWorkViewModel mViewModel;
    @BindViewModel
    MsgViewModel msgViewModel;

    private TitleBar mTitleBar;

    public static TitleFragment newInstance() {

        Bundle args = new Bundle();

        TitleFragment fragment = new TitleFragment();
        fragment.setArguments(args);
        return fragment;
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

//        LogUtils.INSTANCE.e("" + getHasId() + ", " + getSceneId());

        mTitleBar.setBackListener(v -> {
            finish();
        });

    }

    @Override
    public void initViewData() {
        super.initViewData();
        if (mViewModel == null){
            LogUtils.INSTANCE.w(" null ");
        }else {
            LogUtils.INSTANCE.w("---- " + mViewModel);
            mViewModel.reqTeam();
        }
        if (msgViewModel == null){
            LogUtils.INSTANCE.w(" null ");
        }else {
            LogUtils.INSTANCE.w("---- " + msgViewModel);
            msgViewModel.reqMsg("6666666666");
        }

    }

    @Override
    public void initObservable() {
        LogUtils.INSTANCE.e("initObservable");
    }

    @Override
    public void handleEvent(@Nullable Message msg) {
        LogUtils.INSTANCE.e("msg : " + msg.getMsg());
    }

    @Override
    public void onVisible() {
        super.onVisible();
        LogUtils.INSTANCE.e("onVisible");
    }

    @Override
    public void onInVisible() {
        super.onInVisible();
        LogUtils.INSTANCE.e("onInVisible");
    }
}
