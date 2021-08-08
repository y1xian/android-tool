package com.yyxnb.simple.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Description: 盛装Fragment的一个容器(代理)Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 *
 * @author : yyx
 * @date ：2018/6/9
 */
public class ContainerActivity extends BaseActivity {

    private WeakReference<Fragment> mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FrameLayout mFrameLayout = new FrameLayout(this);
        mFrameLayout.setId(android.R.id.content);
        setContentView(mFrameLayout);
    }

    public Fragment initBaseFragment() {
        return null;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void initView(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            Intent intent = getIntent();

            if (intent == null) {
                throw new RuntimeException("you must provide a page info to display");
            }

            mFragment = new WeakReference<>(initBaseFragment());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFragment.clear();
    }
}
