package com.yyxnb.widget.fragments.dialog;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.adapter.ext.RecyclerViewExtKt;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.view.popup.Popup;
import com.yyxnb.view.titlebar.TitleBar;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.config.DataConfig;
import com.yyxnb.widget.popup.CommentBottomPopup;
import com.yyxnb.widget.popup.CustomFullScreenPopup;
import com.yyxnb.widget.popup.RegisterBottomPopup;
import com.yyxnb.widget.popup.VpBottomPopup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 对话框.
 */
public class DialogFragment extends BaseFragment {

    private TitleBar mTitleBar;
    private StringListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static DialogFragment newInstance() {

        Bundle args = new Bundle();

        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_dialog;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mTitleBar = findViewById(R.id.mTitleBar);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mTitleBar.setBackListener(v -> finish());

        mAdapter = new StringListAdapter();
        ItemDecoration decoration = new ItemDecoration(mContext);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        decoration.setDividerHeight(10);
        decoration.setDividerWidth(20);
        RecyclerViewExtKt.wrapGrid(mRecyclerView, mContext, 2, decoration).setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                switch (position) {
                    case 0:
                        new Popup.Builder(mContext).asLoading("带标题")
                                .show();
                        break;
                    case 1:
                        new Popup.Builder(mContext).asConfirm("标题", "内容", () -> {
                            ToastUtils.INSTANCE.normal("确认");
                        }, () -> {
                            ToastUtils.INSTANCE.normal("取消");
                        }).show();
                        break;
                    case 2:
                        new Popup.Builder(mContext).asInputConfirm("标题", "内容", "", "请输入", text -> {
                            ToastUtils.INSTANCE.normal("确认 ：" + text);
                        }, () -> {
                            ToastUtils.INSTANCE.normal("取消");
                        }).show();
                        break;
                    case 3:
                        new Popup.Builder(mContext).asCenterList("标题", new String[]{"序列1", "序列2", "序列3"}, (position1, text) -> {
                            ToastUtils.INSTANCE.normal("选中 " + position1 + "，" + text);
                        }).show();
                        break;
                    case 4:
                        new Popup.Builder(mContext).asCenterList("标题", new String[]{"序列1", "序列2", "序列3"},
                                new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher}, 1,
                                R.mipmap.ic_del, (position1, text) -> {
                                    ToastUtils.INSTANCE.normal("选中 " + position1 + "，" + text);
                                }).show();
                        break;
                    case 5:
                        new Popup.Builder(mContext).asBottomList("标题", new String[]{"序列1", "序列2", "序列3"}, (position1, text) -> {
                            ToastUtils.INSTANCE.normal("选中 " + position1 + "，" + text);
                        }).show();
                        break;
                    case 6:
                        new Popup.Builder(mContext).asBottomList("标题", new String[]{"序列1", "序列2", "序列3"},
                                new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher}, 1,
                                R.mipmap.ic_del, (position1, text) -> {
                                    ToastUtils.INSTANCE.normal("选中 " + position1 + "，" + text);
                                }).show();
                        break;
                    case 7:
                        new Popup.Builder(mContext)
                                .hasStatusBarShadow(false)
                                .autoOpenSoftInput(false)
                                .asCustom(new CustomFullScreenPopup(mContext))
                                .show();
                        break;
                    case 8:
                        new Popup.Builder(mContext)
                                .moveUpToKeyboard(false)
                                .asCustom(new RegisterBottomPopup(mContext))
                                .show();
                        break;
                    case 9:
                        new Popup.Builder(mContext)
                                .moveUpToKeyboard(false)
                                .asCustom(new CommentBottomPopup(mContext))
                                .show();
                        break;
                    case 10:
                        new Popup.Builder(mContext)
                                .moveUpToKeyboard(false)
                                .asCustom(new VpBottomPopup(mContext))
                                .show();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public boolean onItemLongClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                switch (position) {
                    case 0:
                        new Popup.Builder(mContext).asLoading("带标题")
                                .bindLayout(R.layout.popup_center_impl_loading)
                                .show();
                        break;
                    case 1:
                        new Popup.Builder(mContext).asConfirm("标题", "内容", () -> {
                            ToastUtils.INSTANCE.normal("确认");
                        }, () -> {
                            ToastUtils.INSTANCE.normal("取消");
                        }).bindLayout(R.layout.popup_tip_confirm).show();
                        break;
                    case 2:
                        new Popup.Builder(mContext).asInputConfirm("标题", "内容", "", "请输入", text -> {
                            ToastUtils.INSTANCE.normal("确认 ：" + text);
                        }, () -> {
                            ToastUtils.INSTANCE.normal("取消");
                        }).bindLayout(R.layout.popup_tip_confirm).show();
                        break;
                    case 3:

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void initViewData() {
        super.initViewData();
        mAdapter.setDataItems(DataConfig.INSTANCE.getDialogList());
    }
}
