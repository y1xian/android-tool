package com.yyxnb.widget.adapter;

import com.yyxnb.view.proxy.imageloader.ImageHelper;
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
        String url = "http://img0.imgtn.bdimg.com/it/u=4073821464,3431246218&fm=26&gp=0.jpg";

        holder.setText(R.id.tvText, s);
        ImageHelper.INSTANCE.load(s).into(holder.getView(R.id.ivPic));
    }
}
