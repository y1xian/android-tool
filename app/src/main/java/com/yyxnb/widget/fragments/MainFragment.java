package com.yyxnb.widget.fragments;


import android.arch.paging.PagedListAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypePagedAdapter;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.common.Bus;
import com.yyxnb.lib_skin.RecyclerViewSetter;
import com.yyxnb.lib_skin.SkinTheme;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.MainListAdapter;
import com.yyxnb.widget.bean.MainBean;
import com.yyxnb.widget.fragments.dialog.DialogFragment;
import com.yyxnb.widget.fragments.skin.SkinMainFragment;
import com.yyxnb.widget.vm.MainViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
@BindRes
public class MainFragment extends AbsListFragment<MainBean, MainViewModel> {

    private MainListAdapter mAdapter = new MainListAdapter();

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRefreshLayout.setEnableRefresh(false).setEnableLoadMore(false);
        mAdapter.setOnItemClickListener(new MultiItemTypePagedAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                setMenu(mAdapter.getData().get(position).id);
            }
        });

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            if (mAdapter.getData().get(position).type == 1) {
                return 2;
            }
            return 1;
        });
        mRecyclerView.setLayoutManager(manager);
        decoration.setDividerWidth(5);
        decoration.setDividerHeight(5);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(mRecyclerView);

        recyclerViewSetter
                .childViewBgColor(R.id.mLayout, R.attr.colorBackground)
                .childViewBgColor(R.id.mItemLayout, R.attr.colorBackgroundItem)
                .childViewTextColor(R.id.tvText, R.attr.colorText);

        // 构建对象
        SkinTheme theme = new SkinTheme.Builder(getActivity())
                .backgroundColor(R.id.mLayout, R.attr.colorBackground) // 设置view的背景图片
                .textColor(R.id.tvText, R.attr.colorText) // 设置文本颜色
                .setter(recyclerViewSetter)           // 手动设置setter
                .build();

        theme.setTheme(SkinTheme.getCurrentThemeId());

        Bus.observe(this, msgEvent -> {
            if (msgEvent.getCode() == 0x11){
                theme.setTheme((Integer) msgEvent.getData());
            }
        });

    }

    private void setMenu(int position) {
        switch (position) {
            case 11:
                startFragment(new ToastFragment());
                break;
            case 12:
                startFragment(new SkinMainFragment());
                break;
            case 41:
                startFragment(new TitleFragment());
                break;
            case 42:
                startFragment(new DialogFragment());
                break;
            case 43:
                startFragment(new TagFragment());
                break;
            default:
                break;
        }
    }

    @Override
    public PagedListAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

}
