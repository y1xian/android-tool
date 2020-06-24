package com.yyxnb.widget.fragments.adapter;


import android.os.Bundle;

import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.widget.R;
import com.yyxnb.widget.base.BaseFragment;

/**
 * Adapter 列表.
 */
@BindRes(layoutRes = R.layout.fragment_adapter_list)
public class AdapterListFragment extends BaseFragment {
    @Override
    public void initView(Bundle savedInstanceState) {

    }

//    private StringListAdapter mAdapter;
//    private RecyclerView mRecyclerView;
//
//    public static AdapterListFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        AdapterListFragment fragment = new AdapterListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void initView(@Nullable Bundle savedInstanceState) {
//        mRecyclerView = findViewById(R.id.mRecyclerView);
//        mAdapter = new StringListAdapter();
//
//        ItemDecoration decoration = new ItemDecoration(mContext);
//        decoration.setOnlySetItemOffsetsButNoDraw(true);
//        decoration.setDrawBorderTopAndBottom(true);
//        decoration.setDrawBorderLeftAndRight(true);
//        decoration.setDividerHeight(20);
//        decoration.setDividerWidth(20);
////        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext, decoration).setAdapter(mAdapter);
////        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext,itemDecoration -> decoration).setAdapter(mAdapter);
//        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext, itemDecoration -> itemDecoration.setOnlySetItemOffsetsButNoDraw(true)
//                .setDrawBorderTopAndBottom(true)
//                .setDrawBorderLeftAndRight(true)
//                .setDividerHeight(20)
//                .setDividerWidth(20)).setAdapter(mAdapter);
//
//        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
//            @Override
//            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
//                super.onItemClick(view, holder, position);
//                switch (position) {
//                    case 0:
//                        startFragment(AdapterHeaderAndFooterFragment.newInstance());
//                        break;
//                    case 1:
//                        startFragment(MultiAdapterFragment.newInstance());
//                        break;
//                    case 2:
//                        startFragment(AdapterHeaderAndFooterFragment2.newInstance());
//                        break;
//                    case 3:
//                        startFragment(PagingFragment.newInstance());
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void initViewData() {
//        super.initViewData();
//        mAdapter.setDataItems(DataConfig.INSTANCE.getAdapterList());
//    }
}
