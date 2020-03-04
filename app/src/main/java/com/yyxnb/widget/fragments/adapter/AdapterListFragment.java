package com.yyxnb.widget.fragments.adapter;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.adapter.ext.RecyclerViewExtKt;
import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Adapter 列表.
 */
@BindFragment(layoutRes = R.layout.fragment_adapter_list, swipeBack = 1)
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
    public void initView(@Nullable Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter = new StringListAdapter();

        ItemDecoration decoration = new ItemDecoration(mContext);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        decoration.setDividerHeight(20);
        decoration.setDividerWidth(20);
//        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext, decoration).setAdapter(mAdapter);
//        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext,itemDecoration -> decoration).setAdapter(mAdapter);
        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext, itemDecoration -> itemDecoration.setOnlySetItemOffsetsButNoDraw(true)
                .setDrawBorderTopAndBottom(true)
                .setDrawBorderLeftAndRight(true)
                .setDividerHeight(20)
                .setDividerWidth(20)).setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                switch (position) {
                    case 0:
                        startFragment(AdapterHeaderAndFooterFragment.newInstance());
                        break;
                    case 1:
                        startFragment(MultiAdapterFragment.newInstance());
                        break;
                    case 2:
                        startFragment(AdapterHeaderAndFooterFragment2.newInstance());
                        break;
                    case 3:
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
