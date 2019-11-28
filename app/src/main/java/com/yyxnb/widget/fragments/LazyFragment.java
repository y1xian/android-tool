package com.yyxnb.widget.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.utils.log.LogUtils;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 懒加载.
 */
public class LazyFragment extends BaseFragment{

    private RadioGroup radioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isCurrent;

    public static LazyFragment newInstance() {

        Bundle args = new Bundle();

        LazyFragment fragment = new LazyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int initLayoutResId() {
        return R.layout.fragment_lazy;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            getCheckedId(checkedId);
        });
    }

    @Override
    public void initViewData() {
        super.initViewData();

        fragments.add(LazyPageFragment.newInstance());
        fragments.add(LazyPageFragment.newInstance());
        fragments.add(LazyPageFragment.newInstance());
        fragments.add(LazyPageFragment.newInstance());

        changeView(0);

        LogUtils.INSTANCE.w("---initViewData");
    }

    private void getCheckedId(int checkedId) {
        switch (checkedId){
            case R.id.btn_1:
                changeView(0);
                break;
            case R.id.btn_2:
                changeView(1);
                break;
            case R.id.btn_3:
                changeView(2);
                break;
            case R.id.btn_4:
                changeView(3);
                break;
        }
    }

    private void changeView(int index) {
        if (currentIndex == index && isCurrent) {
            //重复点击
            return;
        }
        isCurrent = true;
        //开启事务
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        //隐藏当前Fragment
        ft.hide(fragments.get(currentIndex));
        //判断Fragment是否已经添加
        if (!fragments.get(index).isAdded()){
            ft.add(R.id.mFrameLayout,fragments.get(index)).show(fragments.get(index));
        }else {
            //显示新的Fragment
            ft.show(fragments.get(index));
        }
        ft.commitAllowingStateLoss();
        currentIndex = index;
    }
}
