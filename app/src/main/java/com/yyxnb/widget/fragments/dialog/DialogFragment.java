package com.yyxnb.widget.fragments.dialog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.base.IFragment;
import com.yyxnb.common.AppConfig;
import com.yyxnb.view.popup.Popup;
import com.yyxnb.view.titlebar.TitleBar;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.data.DataConfig;
import com.yyxnb.widget.popup.CommentBottomPopup;
import com.yyxnb.widget.popup.CustomFullScreenPopup;
import com.yyxnb.widget.popup.RegisterBottomPopup;
import com.yyxnb.widget.popup.VpBottomPopup;

/**
 * 对话框.
 */
@BindRes
public class DialogFragment extends Fragment implements IFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    private TitleBar mTitleBar;
    private StringListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void initView(Bundle savedInstanceState) {

        mTitleBar = getView().findViewById(R.id.mTitleBar);
        mRecyclerView = getView().findViewById(R.id.mRecyclerView);

        mTitleBar.setBackListener(v -> getBaseDelegate().finish());

        mAdapter = new StringListAdapter();
        ItemDecoration decoration = new ItemDecoration(getContext());
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        decoration.setDividerHeight(10);
        decoration.setDividerWidth(20);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                switch (position) {
                    case 0:
                        new Popup.Builder(getContext()).asLoading("带标题")
                                .show();
                        break;
                    case 1:
                        new Popup.Builder(getContext()).asConfirm("标题", "内容", () -> {
                            AppConfig.getInstance().toast("确认");
                        }, () -> {
                            AppConfig.getInstance().toast("取消");
                        }).show();
                        break;
                    case 2:
                        new Popup.Builder(getContext()).asInputConfirm("标题", "内容", "", "请输入", text -> {
                            AppConfig.getInstance().toast("确认 ：" + text);
                        }, () -> {
                            AppConfig.getInstance().toast("取消");
                        }).show();
                        break;
                    case 3:
                        new Popup.Builder(getContext()).asCenterList("标题", new String[]{"序列1", "序列2", "序列3"}, (v, position1, text) -> {
                            AppConfig.getInstance().toast("选中 " + position1 + "，" + text);
                        }).show();
                        break;
                    case 4:
                        new Popup.Builder(getContext()).asCenterList("标题", new String[]{"序列1", "序列2", "序列3"},
                                new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher}, 1,
                                R.mipmap.ic_launcher, (v, position1, text) -> {
                                    AppConfig.getInstance().toast("选中 " + position1 + "，" + text);
                                }).show();
                        break;
                    case 5:
                        new Popup.Builder(getContext()).asBottomList("标题", new String[]{"序列1", "序列2", "序列3"}, (v, position1, text) -> {
                            AppConfig.getInstance().toast("选中 " + position1 + "，" + text);
                        }).show();
                        break;
                    case 6:
                        new Popup.Builder(getContext()).asBottomList("标题", new String[]{"序列1", "序列2", "序列3"},
                                new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher}, 1,
                                R.mipmap.ic_launcher, (v, position1, text) -> {
                                    AppConfig.getInstance().toast("选中 " + position1 + "，" + text);
                                }).show();
                        break;
                    case 7:
                        new Popup.Builder(getContext())
                                .hasStatusBarShadow(false)
                                .autoOpenSoftInput(false)
                                .asCustom(new CustomFullScreenPopup(getContext()))
                                .show();
                        break;
                    case 8:
                        new Popup.Builder(getContext())
                                .moveUpToKeyboard(false)
                                .asCustom(new RegisterBottomPopup(getContext()))
                                .show();
                        break;
                    case 9:
                        new Popup.Builder(getContext())
                                .moveUpToKeyboard(false)
                                .asCustom(new CommentBottomPopup(getContext()))
                                .show();
                        break;
                    case 10:
                        new Popup.Builder(getContext())
                                .moveUpToKeyboard(false)
                                .asCustom(new VpBottomPopup(getContext()))
                                .show();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public boolean onItemLongClick(View view, BaseViewHolder holder, int position) {
                switch (position) {
                    case 0:
                        new Popup.Builder(getContext()).asLoading("带标题")
                                .bindLayout(R.layout.popup_center_impl_loading)
                                .show();
                        break;
                    case 1:
                        new Popup.Builder(getContext()).asConfirm("标题", "内容", () -> {
                            AppConfig.getInstance().toast("确认");
                        }, () -> {
                            AppConfig.getInstance().toast("取消");
                        }).bindLayout(R.layout.popup_tip_confirm).show();
                        break;
                    case 2:
                        new Popup.Builder(getContext()).asInputConfirm("标题", "内容", "", "请输入", text -> {
                            AppConfig.getInstance().toast("确认 ：" + text);
                        }, () -> {
                            AppConfig.getInstance().toast("取消");
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
        mAdapter.setDataItems(DataConfig.getDialogList());
    }
}
