package com.yyxnb.oh.arch.config;

import android.graphics.Color;

import com.yyxnb.oh.application.ApplicationUtils;
import com.yyxnb.oh.arch.R;
import com.yyxnb.oh.arch.annotation.BarStyle;
import com.yyxnb.oh.arch.annotation.SwipeStyle;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：yyx
 * 版    本：1.0
 * 日    期：2020/11/20
 * 历    史：
 * 描    述：参数配置，静态参数、方法
 * ================================================
 */
public class ArchOption implements Serializable {

    /**
     * 侧滑
     */
    private final int swipeBack;
    /**
     * 状态栏透明
     */
    private final boolean statusBarTranslucent;
    /**
     * 给系统窗口留出空间
     */
    private final boolean fitsSystemWindows;
    /**
     * 状态栏文字颜色
     */
    private final int statusBarStyle;
    /**
     * 状态栏颜色
     */
    private final int statusBarColor;
    /**
     * 如果状态栏处于白色且状态栏文字也处于白色，避免看不见
     */
    private final int shouldAdjustForWhiteStatusBar;
    /**
     * 虚拟键背景颜色
     */
    private final int navigationBarColor;
    /**
     * 虚拟键颜色
     */
    private final int navigationBarStyle;
    /**
     * 容器Activity，全路径
     */
    private final String containerActivityClassName;


    public ArchOption(Builder builder) {
        this.swipeBack = builder.swipeBack;
        this.statusBarTranslucent = builder.statusBarTranslucent;
        this.fitsSystemWindows = builder.fitsSystemWindows;
        this.statusBarStyle = builder.statusBarStyle;
        this.statusBarColor = builder.statusBarColor;
        this.shouldAdjustForWhiteStatusBar = builder.shouldAdjustForWhiteStatusBar;
        this.navigationBarColor = builder.navigationBarColor;
        this.navigationBarStyle = builder.navigationBarStyle;
        this.containerActivityClassName = builder.containerActivityClassName;
    }


    public static class Builder {

        private int swipeBack = SwipeStyle.EDGE;
        private boolean statusBarTranslucent = true;
        private boolean fitsSystemWindows = false;
        private int statusBarStyle = BarStyle.DARK_CONTENT;
        private int statusBarColor = ApplicationUtils.getApp().getResources().getColor(R.color.statusBar);
        private int shouldAdjustForWhiteStatusBar = Color.parseColor("#4A4A4A");
        private int navigationBarColor = ApplicationUtils.getApp().getResources().getColor(R.color.statusBar);
        private int navigationBarStyle = BarStyle.DARK_CONTENT;
        private String containerActivityClassName;

        public Builder setSwipeBack(int swipeBack) {
            this.swipeBack = swipeBack;
            return this;
        }

        public Builder setStatusBarTranslucent(boolean statusBarTranslucent) {
            this.statusBarTranslucent = statusBarTranslucent;
            return this;
        }

        public Builder setFitsSystemWindows(boolean fitsSystemWindows) {
            this.fitsSystemWindows = fitsSystemWindows;
            return this;
        }

        public Builder setStatusBarStyle(int statusBarStyle) {
            this.statusBarStyle = statusBarStyle;
            return this;
        }

        public Builder setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder setShouldAdjustForWhiteStatusBar(int shouldAdjustForWhiteStatusBar) {
            this.shouldAdjustForWhiteStatusBar = shouldAdjustForWhiteStatusBar;
            return this;
        }

        public Builder setNavigationBarColor(int navigationBarColor) {
            this.navigationBarColor = navigationBarColor;
            return this;
        }

        public Builder setNavigationBarStyle(int navigationBarStyle) {
            this.navigationBarStyle = navigationBarStyle;
            return this;
        }

        public Builder setContainerActivityClassName(String containerActivityClassName) {
            this.containerActivityClassName = containerActivityClassName;
            return this;
        }

        public ArchOption build() {
            return new ArchOption(this);
        }

    }

    public int getSwipeBack() {
        return swipeBack;
    }

    public boolean isStatusBarTranslucent() {
        return statusBarTranslucent;
    }

    public boolean isFitsSystemWindows() {
        return fitsSystemWindows;
    }

    public int getStatusBarStyle() {
        return statusBarStyle;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getShouldAdjustForWhiteStatusBar() {
        return shouldAdjustForWhiteStatusBar;
    }

    public int getNavigationBarColor() {
        return navigationBarColor;
    }

    public int getNavigationBarStyle() {
        return navigationBarStyle;
    }

    public String getContainerActivityClassName() {
        return containerActivityClassName;
    }

}
