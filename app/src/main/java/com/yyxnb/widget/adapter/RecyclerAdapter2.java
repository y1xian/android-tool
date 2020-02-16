package com.yyxnb.widget.adapter;

import android.annotation.SuppressLint;
import android.support.v7.util.DiffUtil;

import com.yyxnb.adapter.BasePagedAdapter;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;

import org.jetbrains.annotations.NotNull;

public class RecyclerAdapter2 extends BasePagedAdapter<TestData> {


    public RecyclerAdapter2() {
        super(R.layout.item_test_list_layout, DIFF_CALLBACK);
    }

    @Override
    protected void bind(@NotNull BaseViewHolder holder, TestData testData, int position) {
        holder.setText(R.id.tvText, " --- 第 " + testData.getId() + " 条 ------- " + testData.getContent());

        addChildClickViewIds(holder, R.id.btnAdd, R.id.btnDelete, R.id.btnTop, R.id.mLinearLayout);
        addChildLongClickViewIds(holder, R.id.btnAdd);
    }

    private static DiffUtil.ItemCallback<TestData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TestData>() {
                @Override
                public boolean areItemsTheSame(TestData oldConcert, TestData newConcert) {
                    return oldConcert.getId() == newConcert.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(TestData oldConcert,
                                                  TestData newConcert) {
                    return oldConcert.equals(newConcert);
                }
            };
}