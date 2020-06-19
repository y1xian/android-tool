package com.yyxnb.widget.skin;

import android.annotation.SuppressLint;
import android.content.res.Resources.Theme;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ViewGroup类型的Setter,用于修改ListView、RecyclerView等ViewGroup类型的Item
 * View,核心思想为遍历每个Item View中的子控件,然后根据用户绑定的view
 * id与属性来将View修改为当前Theme下的最新属性值，达到ViewGroup控件的换肤效果。
 * {@link ViewBackgroundDrawableSetter}
 */
public class ViewGroupSetter extends ViewSetter {

    /**
     * ListView的子试图的Setter
     */
    protected Set<ViewSetter> mItemViewSetters = new HashSet<ViewSetter>();

    /**
     * @param targetView
     * @param resId
     */
    public ViewGroupSetter(ViewGroup targetView, int resId) {
        super(targetView, resId);
    }

    public ViewGroupSetter(ViewGroup targetView) {
        super(targetView, 0);
    }

    /**
     * 设置View的背景色
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public ViewGroupSetter childViewBgColor(int viewId, int colorId) {
        mItemViewSetters.add(new ViewBackgroundColorSetter(viewId, colorId));
        return this;
    }

    /**
     * 设置View的drawable背景
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewGroupSetter childViewBgDrawable(int viewId, int drawableId) {
        mItemViewSetters.add(new ViewBackgroundDrawableSetter(viewId,
                drawableId));
        return this;
    }

    /**
     * 设置文本颜色,因此View的类型必须为TextView或者其子类
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public ViewGroupSetter childViewTextColor(int viewId, int colorId) {
        mItemViewSetters.add(new TextColorSetter(viewId, colorId));
        return this;
    }

    @Override
    public void setValue(Theme newTheme, int themeId) {
        mView.setBackgroundColor(getColor(newTheme));
        // 清空AbsListView的元素
        clearListViewRecyclerBin(mView);
        // 清空RecyclerView
        clearRecyclerViewRecyclerBin(mView);
        // 修改所有子元素的相关属性
        changeChildenAttrs((ViewGroup) mView, newTheme, themeId);
    }

    /**
     * @param viewId
     * @return
     */
    private View findViewById(View rootView, int viewId) {
        View targetView = rootView.findViewById(viewId);
        return targetView;
    }

    /**
     * 修改子视图的对应属性
     *
     * @param viewGroup
     * @param newTheme
     * @param themeId
     */
    private void changeChildenAttrs(ViewGroup viewGroup, Theme newTheme,
                                    int themeId) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            // 深度遍历
            if (childView instanceof ViewGroup) {
                changeChildenAttrs((ViewGroup) childView, newTheme, themeId);
            }
            // 遍历子元素与要修改的属性,如果相同那么则修改子View的属性
            for (ViewSetter setter : mItemViewSetters) {
                // 每次都要从ViewGroup中查找数据
                setter.mView = findViewById(viewGroup, setter.mViewId);
                if (childView.getId() == setter.getViewId()) {
                    setter.setValue(newTheme, themeId);
                }
            }
        }
    }

    @SuppressWarnings({"JavaReflectionMemberAccess", "RedundantArrayCreation"})
    private void clearListViewRecyclerBin(View rootView) {
        if (rootView instanceof AbsListView) {
            try {
                Field localField = AbsListView.class
                        .getDeclaredField("mRecycler");
                localField.setAccessible(true);
                @SuppressLint("PrivateApi")
                Method localMethod = Class.forName(
                        "android.widget.AbsListView$RecycleBin")
                        .getDeclaredMethod("clear", new Class[0]);
                localMethod.setAccessible(true);
                localMethod.invoke(localField.get(rootView), new Object[0]);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @SuppressWarnings("RedundantArrayCreation")
    public void clearRecyclerViewRecyclerBin(View rootView) {
        if (rootView instanceof RecyclerView) {
            try {
                Field localField = RecyclerView.class
                        .getDeclaredField("mRecycler");
                localField.setAccessible(true);
                Method localMethod = Class.forName(
                        "android.support.v7.widget.RecyclerView$Recycler")
                        .getDeclaredMethod("clear", new Class[0]);
                localMethod.setAccessible(true);
                localMethod.invoke(localField.get(rootView), new Object[0]);
                rootView.invalidate();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

}