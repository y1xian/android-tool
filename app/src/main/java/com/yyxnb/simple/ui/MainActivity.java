package com.yyxnb.simple.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.yyxnb.what.log.LogUtils;
import com.yyxnb.what.network.NetworkLiveData;
import com.yyxnb.what.view.tabbar.Tab;
import com.yyxnb.what.view.tabbar.TabBarView;
import com.yyxnb.simple.R;
import com.yyxnb.simple.base.BaseActivity;
import com.yyxnb.simple.ui.fragments.function.FunctionFragment;
import com.yyxnb.simple.ui.fragments.system.SystemFragment;
import com.yyxnb.simple.ui.fragments.tool.ToolFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/08
 * 描    述：主页
 * ================================================
 */
public class MainActivity extends BaseActivity {

    private ArrayList<Fragment> fragments;
    private List<Tab> tabs;

    private TabBarView mTabLayout;
    private int currentIndex;
    private boolean isAddeds;

    @Override
    protected int initLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mTabLayout = findViewById(R.id.tabLayout);

        NetworkLiveData.getInstance().observe(this, integer -> {
            LogUtils.e("网络状态 ： " + integer);
        });
        LogUtils.w("initView");
    }

    @Override
    protected void initViewData() {
        super.initViewData();
        LogUtils.w("initViewData");
        if (null == fragments) {
            fragments = new ArrayList<>();
            fragments.add(new ToolFragment());
            fragments.add(new FunctionFragment());
            fragments.add(new SystemFragment());

            tabs = new ArrayList<>();
            tabs.add(new Tab(this, "控件", R.mipmap.ic_titlebar_progress));
            tabs.add(new Tab(this, "功能", R.mipmap.ic_titlebar_progress));
            tabs.add(new Tab(this, "系统", R.mipmap.ic_titlebar_progress));
        }

        mTabLayout.setTab(tabs);

        mTabLayout.setOnSelectListener((v, position, text) -> changeView(position));

        changeView(0);
    }

    //设置Fragment页面
    private void changeView(int index) {

        if (currentIndex == index && isAddeds) {
            //重复点击
            return;
        }
        isAddeds = true;
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //隐藏当前Fragment
        ft.hide(fragments.get(currentIndex));
        //判断Fragment是否已经添加
        if (!fragments.get(index).isAdded()) {
            ft.add(R.id.flContent, fragments.get(index)).show(fragments.get(index));
        } else {
            //显示新的Fragment
            ft.show(fragments.get(index));
        }
        ft.commitAllowingStateLoss();
        currentIndex = index;
    }
}