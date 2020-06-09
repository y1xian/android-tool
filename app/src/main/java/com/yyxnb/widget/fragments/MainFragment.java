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
import com.yyxnb.widget.adapter.MainListAdapter;
import com.yyxnb.widget.bean.MainBean;
import com.yyxnb.widget.fragments.dialog.DialogFragment;
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
//        decoration.setDrawBorderTopAndBottom(true);
//        decoration.setDrawBorderLeftAndRight(true);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setMenu(int position) {
        switch (position) {
            case 41:
                startFragment(TitleFragment.newInstance());
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
