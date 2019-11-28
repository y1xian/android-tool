package com.yyxnb.widget.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
import com.yyxnb.arch.utils.ToastUtils;
import com.yyxnb.arch.utils.log.LogUtils;
import com.yyxnb.view.rv.ItemDecoration;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;
import com.yyxnb.widget.vm.NetWorkViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 懒加载分页.
 */
public class LazyPageFragment extends BaseFragmentVM<NetWorkViewModel> {

    private StringListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static LazyPageFragment newInstance() {

        Bundle args = new Bundle();

        LazyPageFragment fragment = new LazyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_lazy_page;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter = new StringListAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ItemDecoration decoration = new ItemDecoration(mContext);
        decoration.setDividerColor(Color.BLUE);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                ToastUtils.INSTANCE.normal("第 " + position + " 条");
            }
        });

        mAdapter.setDataItems(DataConfig.INSTANCE.getData());
    }

    @Override
    public void initViewData() {
        super.initViewData();

        mAdapter.setDataItems(DataConfig.INSTANCE.getData());

        LogUtils.INSTANCE.w("-pg--initViewData");
    }

    @Override
    public void onVisible() {
        super.onVisible();

    }

    @Override
    public void onInVisible() {
        super.onInVisible();

    }
}
