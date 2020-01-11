package com.yyxnb.widget.fragments.lazy;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.arch.annotations.SubPage;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.utils.AppConfig;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.view.rv.BaseViewHolder;
import com.yyxnb.view.rv.ItemDecoration;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 懒加载分页.
 */
@SubPage
public class LazyPageFragment extends BaseFragment {

    private StringListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int page;

    public static LazyPageFragment newInstance(int page) {

        Bundle args = new Bundle();
        args.putInt("page",page);
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

        page = getArguments().getInt("page",0);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter = new StringListAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ItemDecoration decoration = new ItemDecoration(mContext);
//        decoration.setDividerColor(Color.BLUE);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                AppConfig.INSTANCE.toast("第 " + position + " 条");
            }
        });

//        mAdapter.setDataItems(DataConfig.INSTANCE.getData());
    }

    @Override
    public void initViewData() {
        super.initViewData();

        mAdapter.setDataItems(DataConfig.INSTANCE.getData());

        LogUtils.INSTANCE.w("-pg--initViewData      " + page);
    }

    @Override
    public void onVisible() {
        super.onVisible();
        LogUtils.INSTANCE.e("-pg--onVisible     " + page);
    }

    @Override
    public void onInVisible() {
        super.onInVisible();
        LogUtils.INSTANCE.e("-pg--onInVisible   " + page);
    }

}
