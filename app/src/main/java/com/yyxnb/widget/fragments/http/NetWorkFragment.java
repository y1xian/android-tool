package com.yyxnb.widget.fragments.http;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yyxnb.arch.base.mvvm.BaseFragmentVM;
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
    private RecyclerView mRecyclerView;

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_net_work;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new NetWorkListAdapter();
//        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initViewData() {
        super.initViewData();

        mViewModel.reqTeam();

        mViewModel.getTestList().observe(this,t -> {
            switch (t.getStatus()) {
                case SUCCESS:
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
