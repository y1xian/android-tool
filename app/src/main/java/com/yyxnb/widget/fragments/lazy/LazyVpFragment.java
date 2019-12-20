package com.yyxnb.widget.fragments.lazy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.yyxnb.arch.annotations.BarStyle;
import com.yyxnb.arch.annotations.StatusBarDarkTheme;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.base.BaseFragmentPagerAdapter;
import com.yyxnb.utils.StatusBarUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.utils.DpUtils;
import com.yyxnb.widget.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 懒加载.vp
 */
@StatusBarDarkTheme(value = BarStyle.LightContent)
public class LazyVpFragment extends BaseFragment {

    private Toolbar mToolbar;
    private MagicIndicator mIndicator;
    private ViewPager mViewPager;
    private String[] title = new String[]{"11","2222","333","44444"};
    private List<Fragment> fragments = new ArrayList<>();

    public static LazyVpFragment newInstance() {

        Bundle args = new Bundle();

        LazyVpFragment fragment = new LazyVpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_lazy_vp;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mIndicator = findViewById(R.id.mIndicator);
        mViewPager = findViewById(R.id.mViewPager);
        mToolbar = findViewById(R.id.mToolbar);

//        StatusBarUtils.INSTANCE.appendStatusBarPadding(mToolbar,100);


        LogUtils.INSTANCE.e("， " + StatusBarUtils.INSTANCE.isStatusBarLightMode(getWindow()));

        mToolbar.post(() -> {
            LogUtils.INSTANCE.e(mToolbar.getHeight() +"， " + mToolbar.getMeasuredHeight());
            StatusBarUtils.INSTANCE.appendStatusBarPadding(mToolbar,mToolbar.getHeight());
        });
    }

    @Override
    public void initViewData() {
        super.initViewData();
        StatusBarUtils.INSTANCE.setStatusBarStyle(getWindow(), false);
        LogUtils.INSTANCE.w("--- LazyVpFragment");

        fragments.add(LazyPageFragment.newInstance(11));
        fragments.add(LazyPageFragment.newInstance(22));
        fragments.add(LazyPageFragment.newInstance(33));
        fragments.add(LazyPageFragment.newInstance(44));

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        //ture 即标题平分屏幕宽度的模式
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(title[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //设置宽度
//                indicator.setLineWidth(DpUtils.dp2px(mContext,30));
                //设置高度
                indicator.setLineHeight(DpUtils.dp2px(mContext,5));
                //设置颜色
                indicator.setColors(Color.parseColor("#FF9241"));
                //设置圆角
                indicator.setRoundRadius(5);
                //设置模式
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator);

        mViewPager.setOffscreenPageLimit(title.length - 1);
        mViewPager.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager(),fragments));
        //与ViewPagger联动
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }
}
