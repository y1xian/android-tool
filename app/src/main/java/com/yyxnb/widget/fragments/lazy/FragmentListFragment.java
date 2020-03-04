package com.yyxnb.widget.fragments.lazy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.utils.AppConfig;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * fragment 列表.
 */
@BindFragment(layoutRes = R.layout.fragment_fragment_list, group = 5)
public class FragmentListFragment extends BaseFragment {

    private StringListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static FragmentListFragment newInstance() {

        Bundle args = new Bundle();

        FragmentListFragment fragment = new FragmentListFragment();
        fragment.setArguments(args);
        return fragment;
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
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                switch (position) {
                    case 0:
                        startFragment(LazyFragment.newInstance());
                        break;
                    case 1:
                        startFragment(LazyTitleVpFragment.newInstance());
                        break;
                    case 2:
                        Bundle bundle = initArguments();
                        bundle.putString("x", "456");
                        startFragment(ForResultFragment.newInstance(), 123);
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
        mAdapter.setDataItems(DataConfig.INSTANCE.getFragmentList());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.INSTANCE.w(" requestCode : " + requestCode + " , resultCode : " + resultCode);
        if (requestCode == 123 && resultCode == 321 && data != null) {
            String s = data.getStringExtra("data");
            AppConfig.INSTANCE.toast(s);
            LogUtils.INSTANCE.w(s);
        }

    }

}
