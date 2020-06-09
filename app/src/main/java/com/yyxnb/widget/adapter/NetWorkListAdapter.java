package com.yyxnb.widget.adapter;


import com.yyxnb.adapter.BaseAdapter;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;



public class NetWorkListAdapter extends BaseAdapter<TestData> {


    public NetWorkListAdapter() {
        super(R.layout.item_test_list_layout);
    }

    @Override
    protected void bind( BaseViewHolder holder, TestData s, int position) {
        holder.setText(R.id.tvText, " --- 第 " + s.getId() + " 条 ------- " + s.getContent());

        addChildClickViewIds(holder, R.id.btnAdd, R.id.btnDelete, R.id.btnTop, R.id.mLinearLayout);
        addChildLongClickViewIds(holder, R.id.btnAdd);
    }
}
