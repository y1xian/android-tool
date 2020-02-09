package com.yyxnb.widget.adapter;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDelegate;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;

import org.jetbrains.annotations.NotNull;

public class MultiItemAdapter extends MultiItemTypeAdapter<TestData> {

    public MultiItemAdapter() {
        addItemDelegate(new ItemDelegate<TestData>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_string_list_layout;
            }

            @Override
            public boolean isThisType(TestData item, int position) {
                return position %3 == 0;
            }

            @Override
            public void bind(@NotNull BaseViewHolder holder, TestData s, int position) {
                holder.setText(R.id.tvText, " --- 第 " +s.getId() +" 条 ------- "+ s.getContent());
            }
        });

        addItemDelegate(new ItemDelegate<TestData>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_multi_list_1_layout;
            }

            @Override
            public boolean isThisType(TestData item, int position) {
                return position %3 == 1;
            }

            @Override
            public void bind(@NotNull BaseViewHolder holder, TestData s, int position) {
                holder.setText(R.id.tvText, " --- 第 " +s.getId() +" 条 ------- "+ s.getContent());
            }
        });

        addItemDelegate(new ItemDelegate<TestData>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_multi_list_2_layout;
            }

            @Override
            public boolean isThisType(TestData item, int position) {
                return position %3 == 2;
            }

            @Override
            public void bind(@NotNull BaseViewHolder holder, TestData s, int position) {
                holder.setText(R.id.tvText, " --- 第 " +s.getId() +" 条 ------- "+ s.getContent());
            }
        });

    }
}
