package com.yyxnb.view.popup.code;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.yyxnb.view.R;
import com.yyxnb.view.popup.Popup;
import com.yyxnb.utils.interfaces.OnSelectListener;
import com.yyxnb.view.rv.BaseAdapter;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.view.rv.ViewHolder;

import java.util.Arrays;

/**
 * Description: 在中间的列表对话框
 */
public class CenterListPopup extends CenterPopup {

    RecyclerView recyclerView;
    TextView tv_title;

    public CenterListPopup(@NonNull Context context) {
        super(context);
    }

    /**
     * 传入自定义的布局，对布局中的id有要求
     *
     * @param layoutId 要求layoutId中必须有一个id为recyclerView的RecyclerView，如果你需要显示标题，则必须有一个id为tv_title的TextView
     * @return
     */
    public CenterListPopup bindLayout(int layoutId) {
        this.bindLayoutId = layoutId;
        return this;
    }

    /**
     * 传入自定义的 item布局
     *
     * @param itemLayoutId 条目的布局id，要求布局中必须有id为iv_image的ImageView，和id为tv_text的TextView
     * @return
     */
    public CenterListPopup bindItemLayout(int itemLayoutId) {
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
        tv_title = findViewById(R.id.tvTitle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        if (tv_title != null) {
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(GONE);
                findViewById(R.id.mDivider).setVisibility(GONE);
            } else {
                tv_title.setText(title);
            }
        }

        final BaseAdapter<String> adapter = new BaseAdapter<String>(bindItemLayoutId == 0 ? R.layout._popup_adapter_text : bindItemLayoutId) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
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
                        holder.setImageResource(R.id.ivCheck, iconCheckId);
                    }
                    holder.<TextView>getView(R.id.tvText).setTextColor(position == checkedPosition ?
                            Popup.getPrimaryColor() : getResources().getColor(R.color.title_color));
                }
                if (position == (data.length - 1)) {
                    holder.getView(R.id.mDivider).setVisibility(INVISIBLE);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (selectListener != null) {
                    if (position >= 0 && position < adapter.getData().size()) {
                        selectListener.onSelect(position, adapter.getData().get(position));
                    }
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataSetChanged();
                }
                if (popupInfo.autoDismiss) {
                    dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setDataItems(Arrays.asList(data));
    }

    String title;
    String[] data;
    int[] iconIds;
    int iconCheckId;

    public CenterListPopup setStringData(String title, String[] data, int[] iconIds, int iconCheckId) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        this.iconCheckId = iconCheckId;
        return this;
    }

    private OnSelectListener selectListener;

    public CenterListPopup setOnSelectListener(OnSelectListener selectListener) {
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
    public CenterListPopup setCheckedPosition(int position) {
        this.checkedPosition = position;
        return this;
    }

    @Override
    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? (int) (super.getMaxWidth() * .8f)
                : popupInfo.maxWidth;
    }
}
