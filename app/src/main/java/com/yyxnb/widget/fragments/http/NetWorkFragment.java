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
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.arch.annotations.BindViewModel;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
import com.yyxnb.http.cache.CacheManager;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.NetWorkListAdapter;
import com.yyxnb.widget.vm.MsgViewModel;
import com.yyxnb.widget.vm.NetWorkViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * 网络列表.
 */
@BindFragment(layoutRes = R.layout.fragment_net_work)
public class NetWorkFragment extends BaseFragment/*VM<NetWorkViewModel>*/ {

    @BindViewModel
    NetWorkViewModel mViewModel;
    @BindViewModel
    MsgViewModel msgViewModel;

    private NetWorkListAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private int page = 1;

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

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
//            @Override
////            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
////                ToastUtils.INSTANCE.normal("" + position);
////            }


            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);

            }

            @Override
            public void onItemChildClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemChildClick(view, holder, position);
                if (view.getId() == R.id.mLinearLayout){
                    msgViewModel.reqToast("第 " + position);
                }
            }
        });


    }

    @Override
    public void initViewData() {
        super.initViewData();

        LogUtils.INSTANCE.e(" aaaaa");

        mViewModel.reqTeam();

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
