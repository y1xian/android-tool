package com.yyxnb.widget.fragments.adapter;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.view.rv.ItemDecoration;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;
import com.yyxnb.widget.fragments.NetWorkFragment;
import com.yyxnb.widget.fragments.network.PagingFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Adapter 列表.
 */
public class AdapterListFragment extends BaseFragment {

    private StringListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static AdapterListFragment newInstance() {

        Bundle args = new Bundle();

        AdapterListFragment fragment = new AdapterListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_adapter_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter = new StringListAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ItemDecoration decoration = new ItemDecoration(mContext);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        decoration.setDividerHeight(20);
        decoration.setDividerWidth(20);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                switch (position) {
                    case 0:
                        startFragment(AdapterHeaderAndFooterFragment.newInstance());
                        break;
                    case 1:
                        startFragment(PagingFragment.newInstance());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initViewData() {
        super.initViewData();
        mAdapter.setDataItems(DataConfig.INSTANCE.getAdapterList());
    }
}
