package com.yyxnb.widget.fragments.http;


import android.os.Bundle;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.http.upload.UploadRetrofit;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * 上传.
 */
public class UploadFragment extends BaseFragment {

    public static UploadFragment newInstance() {

        Bundle args = new Bundle();

        UploadFragment fragment = new UploadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_upload;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //本地图片
        String url = "/storage/emulated/0/MagazineUnlock/magazine-unlock-05-2.3.2862-98D64FEA5AEA01B9532F258EE5AF1980.jpg";

//        UploadRetrofit.INSTANCE.uploadImgsWithParams()
    }

    @Override
    public void initViewData() {
        super.initViewData();

    }
}
