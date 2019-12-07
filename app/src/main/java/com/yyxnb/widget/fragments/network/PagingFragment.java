package com.yyxnb.widget.fragments.network;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
import com.yyxnb.arch.utils.log.LogUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.RecyclerAdapter;
import com.yyxnb.widget.vm.PagingViewModel;

import org.jetbrains.annotations.Nullable;

/**
 * paging.
 */
public class PagingFragment extends BaseFragmentVM<PagingViewModel> {

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static PagingFragment newInstance() {

        Bundle args = new Bundle();

        PagingFragment fragment = new PagingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_paging;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initViewData() {
        super.initViewData();

        mViewModel.getConvertList().observe(this, data -> {
            LogUtils.INSTANCE.e("----mViewModel---" + data.size());
            mAdapter.submitList(data);
        });
    }
}
