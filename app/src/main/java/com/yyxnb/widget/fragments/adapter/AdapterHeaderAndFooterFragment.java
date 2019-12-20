package com.yyxnb.widget.fragments.adapter;


import android.os.Bundle;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * 适配器 头 + 底.
 */
public class AdapterHeaderAndFooterFragment extends BaseFragment {


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

    }
}
