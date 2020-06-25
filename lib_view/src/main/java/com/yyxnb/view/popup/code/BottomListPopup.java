package com.yyxnb.view.popup.code;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.yyxnb.view.R;
import com.yyxnb.common.interfaces.OnSelectListener;
import com.yyxnb.view.popup.Popup;
import com.yyxnb.adapter.BaseAdapter;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypeAdapter;

import java.util.Arrays;

/**
 * Description: 底部的列表对话框
 */
public class BottomListPopup extends BottomPopup {

    RecyclerView recyclerView;
    TextView tvTitle;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    public BottomListPopup(@NonNull Context context) {
        super(context);
    }

    /**
     * 传入自定义的布局，对布局中的id有要求
     *
     * @param layoutId 要求layoutId中必须有一个id为mRecyclerView的RecyclerView，如果你需要显示标题，则必须有一个id为tvTitle的TextView
     */
    public BottomListPopup bindLayout(int layoutId) {
        this.bindLayoutId = layoutId;
        return this;
    }

    /**
     * 传入自定义的 item布局
     *
     * @param itemLayoutId 条目的布局id，要求布局中必须有id为ivCheck的ImageView，和id为tvText的TextView
     */
    public BottomListPopup bindItemLayout(int itemLayoutId) {
        this.bindItemLayoutId = itemLayoutId;
        return this;
    }

    @Override
    protected int initLayoutResId() {
        return bindLayoutId == 0 ? R.layout._popup_center_impl_list : bindLayoutId;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        recyclerView = findViewById(R.id.mRecyclerView);
        tvTitle = findViewById(R.id.tvTitle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        if (tvTitle != null) {
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(GONE);
                findViewById(R.id.mDivider).setVisibility(GONE);
            } else {
                tvTitle.setText(title);
            }
        }

        final BaseAdapter<String> adapter = new BaseAdapter<String>(bindItemLayoutId == 0 ? R.layout._popup_adapter_text : bindItemLayoutId) {
            @Override
            protected void bind(@NonNull BaseViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.tvText, s);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(R.id.ivIcon).setVisibility(VISIBLE);
                    holder.setImageResource(R.id.ivIcon, iconIds[position]);
                } else {
                    holder.getView(R.id.ivIcon).setVisibility(GONE);
                }

                // 对勾View
                if (checkedPosition != -1 && iconCheckId != -1) {
                    if (holder.getView(R.id.ivCheck) != null) {
                        holder.getView(R.id.ivCheck).setVisibility(position == checkedPosition ? VISIBLE : GONE);
                        holder.getView(R.id.ivCheck).setBackgroundResource(iconCheckId);
                    }
                    holder.<TextView>getView(R.id.tvText).setTextColor(position == checkedPosition ?
                            Popup.getPrimaryColor() : getResources().getColor(R.color.colorTitle));
                }
                if (position == (data.length - 1)) {
                    holder.getView(R.id.mDivider).setVisibility(INVISIBLE);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                if (selectListener != null) {
                    selectListener.onClick(view, position, adapter.getData().get(position));
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataSetChanged();
                }
                postDelayed(() -> {
                    if (popupInfo.autoDismiss) {
                        dismiss();
                    }
                }, 100);
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setDataItems(Arrays.asList(data));
    }

    String title;
    String[] data;
    int[] iconIds;
    int iconCheckId;

    public BottomListPopup setStringData(String title, String[] data, int[] iconIds, int iconCheckId) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        this.iconCheckId = iconCheckId;
        return this;
    }

    private OnSelectListener selectListener;

    public BottomListPopup setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    int checkedPosition = -1;

    /**
     * 设置默认选中的位置
     *
     * @param position
     * @return
     */
    public BottomListPopup setCheckedPosition(int position) {
        this.checkedPosition = position;
        return this;
    }


}
