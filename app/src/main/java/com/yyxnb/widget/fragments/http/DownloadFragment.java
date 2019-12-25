package com.yyxnb.widget.fragments.http;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * 下载.
 */
public class DownloadFragment extends BaseFragment {

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_download;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }
}
