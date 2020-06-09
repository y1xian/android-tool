package com.yyxnb.widget.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.base.IFragment;
import com.yyxnb.common.AppConfig;
import com.yyxnb.common.log.LogUtils;
import com.yyxnb.view.text.FlowlayoutTags;
import com.yyxnb.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 */
@BindRes
public class TagFragment extends Fragment implements IFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    private FlowlayoutTags mFlowlayoutTags;
    private FlowlayoutTags mFlowlayoutTags1;
    private FlowlayoutTags mFlowlayoutTags2;


    public static TagFragment newInstance() {

        Bundle args = new Bundle();

        TagFragment fragment = new TagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        mFlowlayoutTags = getView().findViewById(R.id.mFlowlayoutTags);
        mFlowlayoutTags1 = getView().findViewById(R.id.mFlowlayoutTags1);
        mFlowlayoutTags2 = getView().findViewById(R.id.mFlowlayoutTags2);

        final List<String> list = new ArrayList<String>();
        list.add("绿色足球鞋");
        list.add("白色棒球帽");
        list.add("黑色毛衣外套");
        list.add("褐色牛仔连衣裙");
        list.add("白色圆领衬衫");
        list.add("红色长袖连衣裙");

        refreshCategorys(mFlowlayoutTags, list);
        refreshCategorys(mFlowlayoutTags1, list);
        refreshCategorys(mFlowlayoutTags2, list);

        mFlowlayoutTags.setOnTagClickListener(tag -> {
//            AppConfig.getInstance().toast(tag);
            LogUtils.w("tag " + AppConfig.getInstance().getContext());
        });

        mFlowlayoutTags1.setOnTagClickListener(tag -> {
//            AppConfig.getInstance().toast(tag);
            LogUtils.w("tag " + AppConfig.getInstance().getApp());
        });

        mFlowlayoutTags2.setOnTagClickListener(tag -> {
            ArrayList<String> tagList = mFlowlayoutTags2.getCheckedTagsTextsArrayList();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < tagList.size(); i++) {
                s.append(tagList.get(i)).append(",");
            }
            AppConfig.getInstance().toast(s.toString());
        });

    }

    public void refreshCategorys(FlowlayoutTags flowlayoutTags, List<String> list) {
        flowlayoutTags.removeAllViews();

        flowlayoutTags.setTags(list);
        flowlayoutTags.setTagsUncheckedColorAnimal(false);

    }
}
