package com.yyxnb.widget.fragments.adapter;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypePagedAdapter;
import com.yyxnb.adapter.ext.RecyclerViewExtKt;
import com.yyxnb.adapter.rv.BaseRecyclerView;
import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
import com.yyxnb.utils.AppConfig;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.RecyclerAdapter2;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.config.DataConfig;
import com.yyxnb.widget.databinding.FragmentPagingBinding;
import com.yyxnb.widget.vm.PagingViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * paging.
 */
public class PagingFragment extends BaseFragmentVM<PagingViewModel> {

    private RecyclerAdapter2 mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private int page = 1;
    private FragmentPagingBinding binding;

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

        binding = getBinding();

        mRecyclerView = binding.mRecyclerView;
        mRefreshLayout = binding.refreshLayout;

//        mRecyclerView = findViewById(R.id.mRecyclerView);
//        mRefreshLayout = findViewById(R.id.refresh_layout);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerAdapter2();
//        mRecyclerView.setAdapter(mAdapter);

        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext).setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypePagedAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                AppConfig.INSTANCE.toast(" " + position);
            }

            @Override
            public void onItemChildClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemChildClick(view, holder, position);
                TestData data = mAdapter.getData().get(position);
                if (view.getId() == R.id.btnAdd) {
                    int r = new Random().nextInt(100);
                    mAdapter.addDataItem(position + 1, new TestData(r, "666"));
//                    mAdapter.addDataItem(position + 1, DataConfig.INSTANCE.getDataTestData());
//                    mAdapter.updateDataItem(position, new TestData(r, "666"));
//                    mAdapter.clearData();
                    AppConfig.INSTANCE.toast("Add " + position);
                } else if (view.getId() == R.id.btnTop) {
                    mAdapter.changeDataItem(0, data);
                    mRecyclerView.smoothScrollToPosition(0);
                    AppConfig.INSTANCE.toast("btnTop " + position);
                } else if (view.getId() == R.id.btnDelete) {
                    mAdapter.removeDataItem(data);
//                    mAdapter.removeDataItem(position);
                    AppConfig.INSTANCE.toast("btnDelete " + position);
                } else if (view.getId() == R.id.mLinearLayout) {
                    AppConfig.INSTANCE.toast("mLinearLayout " + position + " , hashCode :" + data.hashCode());
                }
            }
        });

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                mAdapter.addDataItem(DataConfig.INSTANCE.getDataTestData());
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.setDataItems(DataConfig.INSTANCE.getDataTestData());
                refreshLayout.finishRefresh();
            }
        });

//        mRecyclerView.setOnRefreshListener(() -> {
//            page = 1;
//            mRecyclerView.setRefreshing(false);
////            setData(DataConfig.INSTANCE.getDataTestData2());
//        }, 500);
//
//        mRecyclerView.setOnLoadMoreListener(() -> {
//
////            if (page % 2 == 0) {
////                mRecyclerView.loadMoreFail();
////            } else if (page == 5) {
//                mRecyclerView.loadMoreEnd();
////            } else {
////                mRecyclerView.loadMoreComplete();
////            }
//
//            page++;
////            setData(DataConfig.INSTANCE.getDataTestData2());
//
//        }, 500);

        mAdapter.addHeaderView(DataConfig.INSTANCE.createView(mContext, "头    第 " + mAdapter.getHeaderCount()));
        mAdapter.addFooterView(DataConfig.INSTANCE.createView(mContext, "尾    第 " + mAdapter.getHeaderCount()));
    }

    @Override
    public void initViewData() {
        super.initViewData();

        mViewModel.getBoundaryPageData().observe(this, aBoolean -> {
            LogUtils.INSTANCE.w("是否有值 ：" + aBoolean);
        });

        mViewModel.getConvertList().observe(this, data -> {
            LogUtils.INSTANCE.e("----mViewModel---" + data.size());
            mAdapter.submitList(data);
        });
    }


}
