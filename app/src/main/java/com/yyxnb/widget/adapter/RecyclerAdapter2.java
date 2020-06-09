package com.yyxnb.widget.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.yyxnb.adapter.BasePagedAdapter;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.databinding.ItemTestListLayoutBinding;


public class RecyclerAdapter2 extends BasePagedAdapter<TestData> {

    private ItemTestListLayoutBinding binding;

    public RecyclerAdapter2() {
        super(R.layout.item_test_list_layout, DIFF_CALLBACK);
    }

//    @Override
//    public void onViewHolderCreated(@NotNull BaseViewHolder holder, @NotNull ViewGroup parent, int viewType) {
//        super.onViewHolderCreated(holder, parent, viewType);
//        DataBindingUtil.bind(holder.getConvertView());
//    }

    @Override
    protected void bind(BaseViewHolder holder, TestData testData, int position) {
//        holder.setText(R.id.tvText, " --- 第 " + testData.getId() + " 条 ------- " + testData.getContent());

        binding = holder.getBinding();
        if (binding != null) {
            binding.setData(testData);
            // 使数据绑定刷新所有挂起的更改
//            binding.executePendingBindings();
        }

        addChildClickViewIds(holder, R.id.btnAdd, R.id.btnDelete, R.id.btnTop, R.id.mLinearLayout);
        addChildLongClickViewIds(holder, R.id.btnAdd);
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