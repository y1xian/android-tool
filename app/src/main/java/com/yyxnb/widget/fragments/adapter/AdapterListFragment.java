package com.yyxnb.widget.fragments.adapter;


import android.os.Bundle;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * Adapter 列表.
 */
public class AdapterListFragment extends BaseFragment {

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

    }
}
