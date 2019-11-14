package com.yyxnb.widget.adapter;

import android.support.v7.util.DiffUtil;

import com.yyxnb.view.rv.BaseAdapter;
import com.yyxnb.view.rv.ViewHolder;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;

import org.jetbrains.annotations.NotNull;

public class NetWorkListAdapter extends BaseAdapter<TestData> {

    public NetWorkListAdapter() {
        super(R.layout.item_test_list_layout, DIFF_CALLBACK);
    }

    @Override
    protected void bind(@NotNull ViewHolder holder, TestData s, int position) {
        holder.setText(R.id.tvText, " --- 第 " +s.getId() +" 条 ------- "+ s.getContent());
    }

    private static DiffUtil.ItemCallback<TestData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TestData>() {
                @Override
                public boolean areItemsTheSame(TestData oldConcert, TestData newConcert) {
                    return oldConcert.getId() == newConcert.getId();
                }

                @Override
                public boolean areContentsTheSame(TestData oldConcert,
                                                  TestData newConcert) {
                    return oldConcert.equals(newConcert);
                }
            };
}
