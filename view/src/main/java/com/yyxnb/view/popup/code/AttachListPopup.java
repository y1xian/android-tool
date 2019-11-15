package com.yyxnb.view.popup.code;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.yyxnb.view.R;
import com.yyxnb.view.popup.interfaces.OnSelectListener;
import com.yyxnb.view.rv.BaseAdapter;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.view.rv.ViewHolder;

import java.util.Arrays;

/**
 * Description: Attach类型的列表弹窗
 */
public class AttachListPopup extends AttachPopup {

    RecyclerView recyclerView;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    public AttachListPopup(@NonNull Context context) {
        super(context);
    }

    /**
     * 传入自定义的布局，对布局中的id有要求
     *
     * @param layoutId 要求layoutId中必须有一个id为recyclerView的RecyclerView，如果你需要显示标题，则必须有一个id为tv_title的TextView
     * @return
     */
    public AttachListPopup bindLayout(int layoutId) {
        this.bindLayoutId = layoutId;
        return this;
    }

    /**
     * 传入自定义的 item布局
     *
     * @param itemLayoutId 条目的布局id，要求布局中必须有id为iv_image的ImageView，和id为tv_text的TextView
     * @return
     */
    public AttachListPopup bindItemLayout(int itemLayoutId) {
        this.bindItemLayoutId = itemLayoutId;
        return this;
    }

    @Override
    protected int initLayoutResId() {
        return bindLayoutId == 0 ? R.layout._popup_attach_impl_list : bindLayoutId;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        recyclerView = findViewById(R.id.mRecyclerView);
//        recyclerView.setupDivider();
        final BaseAdapter<String> adapter = new BaseAdapter<String>(bindItemLayoutId == 0 ? R.layout._popup_adapter_text : bindItemLayoutId) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.tvText, s);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(R.id.ivIcon).setVisibility(VISIBLE);
                    holder.getView(R.id.ivIcon).setBackgroundResource(iconIds[position]);
                } else {
                    holder.getView(R.id.ivIcon).setVisibility(GONE);
                }
                holder.getView(R.id.mDivider).setVisibility(GONE);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getData().get(position));
                }
                if (popupInfo.autoDismiss) {
                    dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setDataItems(Arrays.asList(data));
    }

    String[] data;
    int[] iconIds;

    public AttachListPopup setStringData(String[] data, int[] iconIds) {
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    public AttachListPopup setOffsetXAndY(int offsetX, int offsetY) {
        this.defaultOffsetX += offsetX;
        this.defaultOffsetY += offsetY;
        return this;
    }

    private OnSelectListener selectListener;

    public AttachListPopup setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }
}
