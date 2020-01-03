package com.yyxnb.widget.adapter;


import android.view.View;

import com.yyxnb.utils.ToastUtils;
import com.yyxnb.view.rv.BaseAdapter;
import com.yyxnb.view.rv.BaseViewHolder;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;

import org.jetbrains.annotations.NotNull;

public class NetWorkListAdapter extends BaseAdapter<TestData> {


    public NetWorkListAdapter() {
        super(R.layout.item_test_list_layout);
    }

    @Override
    protected void bind(@NotNull BaseViewHolder holder, TestData s, int position) {
        holder.setText(R.id.tvText, " --- 第 " +s.getId() +" 条 ------- "+ s.getContent());

        addChildClickViewIds(R.id.btnDel,R.id.btnAdd);
    }
}
