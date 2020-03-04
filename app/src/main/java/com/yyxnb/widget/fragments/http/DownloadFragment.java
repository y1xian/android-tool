package com.yyxnb.widget.fragments.http;


import android.os.Bundle;

import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * 下载.
 */
@BindFragment(layoutRes = R.layout.fragment_download)
public class DownloadFragment extends BaseFragment {

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }
}
