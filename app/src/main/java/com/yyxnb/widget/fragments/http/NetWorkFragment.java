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
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.NetWorkListAdapter;
import com.yyxnb.widget.vm.NetWorkViewModel;

import org.jetbrains.annotations.NotNull;
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

        View view = LayoutInflater.from(mContext).inflate(R.layout._loading_layout_empty, (ViewGroup) getMRootView(),false);
//        mAdapter.setEmptyView(view);
        mAdapter.setEmptyView(R.layout._loading_layout_empty);
//        mAdapter.addFootView(view);


        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mAdapter.setDataItems(null);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (page == 1){
                    mViewModel.reqTeam();
                }else {
                    mAdapter.clearAllData();
                }
            }
        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
                ToastUtils.INSTANCE.normal("" + position);
            }

            @Override
            public void onItemChildClick(@Nullable MultiItemTypeAdapter<?> adapter, @Nullable View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (view.getId() == R.id.btnDel){
                    ToastUtils.INSTANCE.normal("Del " + position);
                }else if (view.getId() == R.id.btnDel1){
                    ToastUtils.INSTANCE.normal("Del2 " + position);
                }
            }
        });

        mAdapter.setDataItems(null);
    }

    @Override
    public void initViewData() {
        super.initViewData();



        mViewModel.getTestList().observe(this,t -> {
            switch (t.getStatus()) {
                case SUCCESS:
                    page++;
                    mRefreshLayout.finishRefresh();
                    mAdapter.setDataItems(t.getData().getData());
                    break;
                case ERROR:
                    break;
                case LOADING:
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
}
