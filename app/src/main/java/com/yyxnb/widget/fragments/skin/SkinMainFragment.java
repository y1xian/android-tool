package com.yyxnb.widget.fragments.skin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.common.Bus;
import com.yyxnb.arch.common.MsgEvent;
import com.yyxnb.common.log.LogUtils;
import com.yyxnb.lib_skin.RecyclerViewSetter;
import com.yyxnb.lib_skin.SkinTheme;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.data.DataConfig;
import com.yyxnb.widget.databinding.FragmentSkinMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 换肤.
 */
@BindRes(layoutRes = R.layout.fragment_skin_main)
public class SkinMainFragment extends BaseFragment {

    private FragmentSkinMainBinding binding;

    private RecyclerView mRecyclerView;
    private StringListAdapter mAdapter;
    private List<String> mNewsList = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();
        mRecyclerView = binding.mRecyclerView;

//        ViewGroupSetter recyclerViewSetter = new ViewGroupSetter(mRecyclerView);
        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(mRecyclerView);

        recyclerViewSetter
                .childViewBgColor(R.id.mItemLayout, R.attr.colorBackgroundItem)
                .childViewTextColor(R.id.tvTitle, R.attr.colorTitle)
                .childViewTextColor(R.id.tvText, R.attr.colorText)
                .childViewTextColor(R.id.tvHint, R.attr.colorHint);

        // 构建对象
        SkinTheme theme = new SkinTheme.Builder(getActivity())
                .backgroundColor(R.id.mLayout, R.attr.colorBackground) // 设置view的背景图片
//                .backgroundColor(R.id.change_btn, R.attr.btn_bg) // 设置按钮的背景色
                .textColor(R.id.tvTitle, R.attr.colorTitle) // 设置文本颜色
                .textColor(R.id.tvText, R.attr.colorText) // 设置文本颜色
                .textColor(R.id.tvHint, R.attr.colorHint) // 设置文本颜色
                .setter(recyclerViewSetter)           // 手动设置setter
                .build();

        mAdapter = new StringListAdapter();
        ItemDecoration decoration = new ItemDecoration(getContext());
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        decoration.setDividerHeight(10);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

//        getActivity().setTheme(R.style.NightTheme);

        LogUtils.w(" SkinTheme.getCurrentThemeId() " + SkinTheme.getCurrentThemeId() + " ， " + R.style.NightTheme + " ， "
                + (SkinTheme.getCurrentThemeId() == R.style.NightTheme));
        binding.checkBox.setChecked(SkinTheme.getCurrentThemeId() == R.style.NightTheme);

        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                theme.setTheme(R.style.DayTheme);
            } else {
                theme.setTheme(R.style.NightTheme);
            }
            Bus.post(new MsgEvent(0x11, SkinTheme.getCurrentThemeId()));
        });
    }

    @Override
    public void initViewData() {
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                mAdapter.setDataItems(DataConfig.getDialogList());
                mAdapter.addDataItem(DataConfig.getDialogList());
            }
            return false;
        }
    });

}