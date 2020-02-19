package com.yyxnb.widget.fragments.http;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
import com.yyxnb.http.cache.CacheManager;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.NetWorkListAdapter;
import com.yyxnb.widget.vm.NetWorkViewModel;

import org.jetbrains.annotations.Nullable;


/**
 * 网络列表.
 */
public class NetWorkFragment extends BaseFragmentVM<NetWorkViewModel> {

    private NetWorkListAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private int page = 1;


    @Override
    public int initLayoutResId() {
        return R.layout.fragment_net_work;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new NetWorkListAdapter();
//        mAdapter = new RecyclerAdapter();

        mRecyclerView.setAdapter(mAdapter);

        View view = LayoutInflater.from(mContext).inflate(R.layout._loading_layout_empty, (ViewGroup) getMRootView(), false);
//        mAdapter.setEmptyView(view);
//        mAdapter.setEmptyView(R.layout._loading_layout_empty);
//        mAdapter.addFootView(view);


        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                mAdapter.setDataItems(null);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.reqTeam();
            }
        });

//        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
//            @Override
//            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
//                ToastUtils.INSTANCE.normal("" + position);
//            }
//
//        });

        mViewModel.reqTeam();
    }

    @Override
    public void initViewData() {
        super.initViewData();

        LogUtils.INSTANCE.e(CacheManager.cacheSize() + " 条");


        mViewModel.getTestList().observe(this, t -> {
            switch (t.getStatus()) {
                case SUCCESS:
                    page++;
                    mRefreshLayout.finishRefresh();
                    if (t.getData() != null) {
                        LogUtils.INSTANCE.w(" success   " + (t.getData().getData().size() > 0));
                        LogUtils.INSTANCE.list(t.getData().getData());
                        mAdapter.setDataItems(t.getData().getData());
                    }
                    break;
                case ERROR:
                    break;
                case LOADING:
                    if (t.getData() != null){
                        LogUtils.INSTANCE.w(" loading   " + (t.getData().getData().size() > 0));
                        mAdapter.setDataItems(t.getData().getData());
                    }
                    break;
            }
        });

        LogUtils.INSTANCE.w("---initViewData   n   ");
    }

    public static NetWorkFragment newInstance() {

        Bundle args = new Bundle();

        NetWorkFragment fragment = new NetWorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
//        CacheManager.deleteKey("getTestList");
        super.onDestroy();
    }
}
