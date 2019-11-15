package com.yyxnb.widget.adapter;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;

public class RecyclerAdapter extends PagedListAdapter<TestData, RecyclerAdapter.RecyclerViewHolder> {


    public RecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_list_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        TestData s = getItem(position);
        if (s != null) {
            holder.tvText.setText(" --- 第 " +s.getId() +" 条 ------- "+ s.getContent());
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvText;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
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