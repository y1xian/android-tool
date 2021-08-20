package com.yyxnb.what.arch.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

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
 * 描    述：FragmentLifecycleCallbacks 监听 Fragment 生命周期
 * PS ：先走 Fragment 再走 FragmentLifecycleCallbacks
 * ================================================
 */
public class FragmentLifecycleImpl implements IFragmentLifecycle {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    public FragmentLifecycleImpl(Fragment fragment, FragmentManager fragmentManager) {
        this.mFragment = fragment;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public void onAttached(Context context) {
        PageHelper.getInstance().onAttach(mFragment, context);
        mFragment.getLifecycle().addObserver(this);
    }

    @Override
    public void onCreated(Bundle savedInstanceState) {
//        initDeclaredFields();
        PageHelper.getInstance().onCreate(mFragment, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        PageHelper.getInstance().onViewCreated(mFragment, view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        PageHelper.getInstance().onActivityCreated(mFragment, savedInstanceState);
    }

    @Override
    public void onStarted() {
    }

    @Override
    public void onResumed() {
        PageHelper.getInstance().onResume(mFragment);
    }

    @Override
    public void onPaused() {
        PageHelper.getInstance().onPause(mFragment);
    }

    @Override
    public void onStopped() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        PageHelper.getInstance().onSaveInstanceState(mFragment, outState);
    }

    @Override
    public void onViewDestroyed() {
    }

    @Override
    public void onDestroyed() {
        PageHelper.getInstance().onDestroy(mFragment);
        mFragment.getLifecycle().removeObserver(this);
        this.mFragmentManager = null;
        this.mFragment = null;
    }

    @Override
    public void onDetached() {
    }

    @Override
    public boolean isAdd() {
        return mFragment.isAdded();
    }

    public void initDeclaredFields() {
        LogUtils.w("initDeclaredFields");
        Field[] declaredFields = mFragment.getClass().getDeclaredFields();
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
                    field.set(mFragment, getViewModel(field, viewModel.isActivity()));
                } catch (IllegalAccessException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
    }

    private ViewModel getViewModel(Field field, boolean isActivity) {
        if (isActivity) {
            return ViewModelFactory.createViewModel(mFragment.getActivity(), field);
        } else {
            return ViewModelFactory.createViewModel(mFragment, field);
        }
    }
}
