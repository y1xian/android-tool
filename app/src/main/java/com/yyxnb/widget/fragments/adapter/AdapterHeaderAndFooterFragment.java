package com.yyxnb.widget.fragments.adapter;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.utils.AppConfig;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.view.rv.BaseViewHolder;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.NetWorkListAdapter;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.config.DataConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

/**
 * 适配器 头 + 底.
 */
public class AdapterHeaderAndFooterFragment extends BaseFragment {

    private NetWorkListAdapter mAdapter;
    private TextView tvAddHeader;
    private TextView tvAddFooter;
    private TextView tvClear;
    private TextView tvAddData;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    public static AdapterHeaderAndFooterFragment newInstance() {

        Bundle args = new Bundle();

        AdapterHeaderAndFooterFragment fragment = new AdapterHeaderAndFooterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_adapter_header_and_footer;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        tvAddHeader = findViewById(R.id.tvAddHeader);
        tvAddFooter = findViewById(R.id.tvAddFooter);
        tvClear = findViewById(R.id.tvClear);
        tvAddData = findViewById(R.id.tvAddData);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mAdapter = new NetWorkListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        View view = LayoutInflater.from(mContext).inflate(R.layout._loading_layout_error, (ViewGroup) getMRootView(), false);
//        mAdapter.setEmptyView(R.layout._loading_layout_error);
//        mAdapter.setEmptyView(view);
//        view.setOnClickListener(v -> {
//            ToastUtils.INSTANCE.normal("213");
//        });

        tvAddHeader.setOnClickListener(v -> {
            mAdapter.addHeaderView(createView("头    第 " + mAdapter.getHeadersCount(), true));
        });

        tvAddFooter.setOnClickListener(v -> {
            mAdapter.addFootView(createView("尾    第 " + mAdapter.getFootersCount(), false));
        });

        tvClear.setOnClickListener(v -> {
            mAdapter.clearData();
        });

        tvAddData.setOnClickListener(v -> {

//            mAdapter.addDataItem(0, new TestData(new Random().nextInt(100), "666"));
            mAdapter.addDataItem(DataConfig.INSTANCE.getDataTestData2());
            LogUtils.INSTANCE.w("size " + mAdapter.getDataCount());
            mAdapter.notifyDataSetChanged();
        });

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mAdapter.addDataItem(DataConfig.INSTANCE.getDataTestData2());
                refreshLayout.finishLoadMore(200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.setDataItems(DataConfig.INSTANCE.getDataTestData(), true);
                refreshLayout.finishRefresh(200);
            }
        });


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
//            @Override
//            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
//                ToastUtils.INSTANCE.normal("" + position);
//                LogUtils.INSTANCE.w("" + mAdapter.getChildClickViewIds().toString());
//            }

            @Override
            public void onItemChildClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemChildClick(view, holder, position);
                if (view.getId() == R.id.btnAdd) {
                    mAdapter.addDataItem(position + 1, new TestData(new Random().nextInt(100), "666"));
                    AppConfig.INSTANCE.toast("Add " + position);
                } else if (view.getId() == R.id.btnTop) {
                    mAdapter.changeDataItem(0, mAdapter.getData().get(position));
                    AppConfig.INSTANCE.toast("btnTop " + position);
                } else if (view.getId() == R.id.btnDelete) {
                    mAdapter.removeDataItem(position);
                    AppConfig.INSTANCE.toast("btnDelete " + position);
                } else if (view.getId() == R.id.mLinearLayout) {
                    AppConfig.INSTANCE.toast("" + position);
                }
            }
        });

//        mAdapter.addHeaderView(createView("头    第 " + mAdapter.getHeadersCount(), true));

        new Handler().postDelayed(() -> {
//            mAdapter.addDataItem(DataConfig.INSTANCE.getDataTestData2());
            mAdapter.setDataItems(DataConfig.INSTANCE.getDataTestData(), true);
//            mAdapter.setNewDataItems(DataConfig.INSTANCE.getDataTestData());
//            mAdapter.setDataItems(new ArrayList<>(),true);
//            mAdapter.notifyDataSetChanged();
        }, 1000);


    }

    @Override
    public void initViewData() {
        super.initViewData();


    }

    private TextView createView(String text, Boolean isHeader) {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
//        textView.setPadding(80, 80, 80, 80);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        switch (new Random().nextInt(4)) {
            case 0:
                textView.setBackgroundResource(R.color.red);
                break;
            case 1:
                textView.setBackgroundResource(R.color.yellow);
                break;
            case 2:
                textView.setBackgroundResource(R.color.blue);
                break;
            default:
                textView.setBackgroundResource(R.color.black);
                break;
        }
        textView.setText(text);
        textView.setOnClickListener(v -> {
            if (isHeader) {
//                mAdapter
            } else {

            }
            AppConfig.INSTANCE.toast(text);
        });
        return textView;
    }
}
