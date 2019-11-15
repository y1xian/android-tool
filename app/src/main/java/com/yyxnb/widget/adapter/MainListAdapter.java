package com.yyxnb.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.yyxnb.view.rv.BaseAdapter;
import com.yyxnb.view.rv.ViewHolder;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.NotNull;

public class MainListAdapter extends BaseAdapter<String> {

    public MainListAdapter() {
        super(R.layout.item_main_list_layout);
    }

    @Override
    protected void bind(@NotNull ViewHolder holder, String s, int position) {
        holder.setText(R.id.tvText, s);
    }
}
