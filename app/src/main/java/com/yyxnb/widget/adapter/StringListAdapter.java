package com.yyxnb.widget.adapter;

import com.yyxnb.adapter.BaseAdapter;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.widget.R;


public class StringListAdapter extends BaseAdapter<String> {

    public StringListAdapter() {
        super(R.layout.item_string_list_layout);
    }

    @Override
    protected void bind(BaseViewHolder holder, String s, int position) {
        holder.setText(R.id.tvText, s);
    }
}
