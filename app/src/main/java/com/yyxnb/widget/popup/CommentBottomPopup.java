package com.yyxnb.widget.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.adapter.ext.RecyclerViewExtKt;
import com.yyxnb.view.popup.PopupUtils;
import com.yyxnb.view.popup.code.BottomPopup;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;

/**
 * 评论
 */
public class CommentBottomPopup extends BottomPopup {

    private RecyclerView mRecyclerView;
    private StringListAdapter mAdapter;

    public CommentBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int initLayoutResId() {
        return R.layout.popup_comment_bottom_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.ivDel).setOnClickListener(v -> dismiss());
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mAdapter = new StringListAdapter();
        ItemDecoration decoration = new ItemDecoration(getContext());
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        decoration.setDividerHeight(10);
        decoration.setDividerWidth(20);

        RecyclerViewExtKt.wrapLinear(mRecyclerView, getContext(), decoration).setAdapter(mAdapter);


        mAdapter.setDataItems(DataConfig.INSTANCE.getData());
    }

    @Override
    protected int getPopupHeight() {
        return (int) (PopupUtils.getWindowHeight(getContext()) * .70f);
    }
}
