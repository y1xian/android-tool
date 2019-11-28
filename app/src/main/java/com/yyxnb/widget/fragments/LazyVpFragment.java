package com.yyxnb.widget.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.base.BaseFragmentPagerAdapter;
import com.yyxnb.view.utils.DpUtils;
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
public class LazyVpFragment extends BaseFragment {

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
    }

    @Override
    public void initViewData() {
        super.initViewData();

        fragments.add(LazyPageFragment.newInstance());
        fragments.add(LazyPageFragment.newInstance());
        fragments.add(LazyPageFragment.newInstance());
        fragments.add(LazyPageFragment.newInstance());

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
