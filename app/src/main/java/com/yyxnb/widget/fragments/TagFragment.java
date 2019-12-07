package com.yyxnb.widget.fragments;


import android.os.Bundle;

import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.utils.ToastUtils;
import com.yyxnb.view.text.FlowlayoutTags;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 */
public class TagFragment extends BaseFragment {

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
    public int initLayoutResId() {
        return R.layout.fragment_tag;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mFlowlayoutTags = findViewById(R.id.mFlowlayoutTags);
        mFlowlayoutTags1 = findViewById(R.id.mFlowlayoutTags1);
        mFlowlayoutTags2 = findViewById(R.id.mFlowlayoutTags2);

        final List<String> list = new ArrayList<String>();
        list.add("绿色足球鞋");
        list.add("白色棒球帽");
        list.add("黑色毛衣外套");
        list.add("褐色牛仔连衣裙");
        list.add("白色圆领衬衫");
        list.add("红色长袖连衣裙");

        refreshCategorys(mFlowlayoutTags,list);
        refreshCategorys(mFlowlayoutTags1,list);
        refreshCategorys(mFlowlayoutTags2,list);

        mFlowlayoutTags.setOnTagClickListener(ToastUtils.INSTANCE::normal);

        mFlowlayoutTags1.setOnTagClickListener(ToastUtils.INSTANCE::normal);

        mFlowlayoutTags2.setOnTagClickListener(tag -> {
            ArrayList<String> tagList = mFlowlayoutTags2.getCheckedTagsTextsArrayList();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < tagList.size(); i++) {
                s.append(tagList.get(i)).append(",");
            }
            ToastUtils.INSTANCE.normal(s.toString());
        });


    }

    public void refreshCategorys(FlowlayoutTags flowlayoutTags,List<String> list) {
        flowlayoutTags.removeAllViews();

        flowlayoutTags.setTags(list);
        flowlayoutTags.setTagsUncheckedColorAnimal(false);

    }
}
