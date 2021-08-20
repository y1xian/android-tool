package com.yyxnb.what.arch.lifecycle;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.yyxnb.what.arch.annotation.BindViewModel;
import com.yyxnb.what.arch.helper.PageHelper;
import com.yyxnb.what.arch.viewmodel.ViewModelFactory;
import com.yyxnb.what.log.LogUtils;

import java.lang.reflect.Field;

/**
 * ================================================
 * 作    者：yyx
 * 版    本：1.0
 * 日    期：2020/11/21
 * 历    史：
 * 描    述：ActivityLifecycleCallbacks 监听 Activity 生命周期
 * PS ：先走 ActivityLifecycleCallbacks 再走 Activity
 * ================================================
 */
public class ActivityLifecycleImpl implements IActivityLifecycle {

    private FragmentActivity mActivity;

    public ActivityLifecycleImpl(FragmentActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        initDeclaredFields();
        PageHelper.getInstance().onCreate(mActivity, savedInstanceState);
        this.mActivity.getLifecycle().addObserver(this);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
        PageHelper.getInstance().onResume(mActivity);
    }

    @Override
    public void onPause() {
        PageHelper.getInstance().onPause(mActivity);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onSaveInstanceState(Activity activity, Bundle outState) {
        PageHelper.getInstance().onSaveInstanceState(mActivity, outState);
    }

    @Override
    public void onDestroy() {
        PageHelper.getInstance().onDestroy(mActivity);
        this.mActivity.getLifecycle().removeObserver(this);
        this.mActivity = null;
    }

    public void initDeclaredFields() {
        LogUtils.w("initDeclaredFields");
        Field[] declaredFields = mActivity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            // 允许修改反射属性
            field.setAccessible(true);

            /*
             *  根据 {@link BindViewModel } 注解, 查找注解标示的变量（ViewModel）
             *  并且 创建 ViewModel 实例, 注入到变量中
             */
            final BindViewModel viewModel = field.getAnnotation(BindViewModel.class);
            if (viewModel != null) {
                try {
                    field.set(mActivity, ViewModelFactory.createViewModel(mActivity, field));
                } catch (IllegalAccessException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
    }

}

