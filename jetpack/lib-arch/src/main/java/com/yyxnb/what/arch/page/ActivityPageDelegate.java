package com.yyxnb.what.arch.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/05/12
 * 描    述：Activity 代理
 * ================================================
 */
class ActivityPageDelegate extends PageDelegate {

    private final FragmentActivity mActivity;
    private boolean mIsResumed = false;

    ActivityPageDelegate(@NonNull FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mActivity;
//        UITask.post(() -> {
//            // 配置状态栏、底部虚拟按键信息
//            if (!OPTION.isFitsSystemWindows()) {
//                // 不留空间 则透明
//                StatusBarUtils.setStatusBarColor(mActivity.getWindow(), Color.TRANSPARENT);
//            } else {
//                int statusBarColor = OPTION.getStatusBarColor();
//                //不为深色
//                boolean shouldAdjustForWhiteStatusBar = !StatusBarUtils.isBlackColor(statusBarColor, 176);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    shouldAdjustForWhiteStatusBar = shouldAdjustForWhiteStatusBar
//                            && OPTION.getNavigationBarStyle() == BarStyle.LIGHT_CONTENT;
//                }
//                // 如果状态栏处于白色且状态栏文字也处于白色，避免看不见
//                if (shouldAdjustForWhiteStatusBar) {
//                    statusBarColor = OPTION.getShouldAdjustForWhiteStatusBar();
//                }
//                StatusBarUtils.setStatusBarColor(mActivity.getWindow(), statusBarColor);
//            }
//            StatusBarUtils.setStatusBarStyle(mActivity.getWindow(), OPTION.getNavigationBarStyle() == BarStyle.DARK_CONTENT);
//            StatusBarUtils.setStatusBarTranslucent(mActivity.getWindow(), OPTION.isStatusBarTranslucent(), OPTION.isFitsSystemWindows());
//            StatusBarUtils.setNavigationBarColor(mActivity.getWindow(), OPTION.getNavigationBarColor());
//            StatusBarUtils.setNavigationBarStyle(mActivity.getWindow(), OPTION.getNavigationBarStyle() == BarStyle.DARK_CONTENT);
//
//        });
    }

    @Override
    public void onResume() {
        mIsResumed = true;
    }

    @Override
    public void onPause() {
        mIsResumed = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean isResumed() {
        return mIsResumed;
    }

    @Override
    public void setResult(int resultCode, Intent result) {
        throwException(new PageException("setResult() method can only be called by Fragment"));
    }

}
