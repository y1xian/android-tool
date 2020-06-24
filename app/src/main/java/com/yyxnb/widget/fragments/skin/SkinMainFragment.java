package com.yyxnb.widget.fragments.skin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.common.SPUtils;
import com.yyxnb.common.log.LogUtils;
import com.yyxnb.skinloader.SkinManager;
import com.yyxnb.skinloader.util.AssetFileUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.StringListAdapter;
import com.yyxnb.widget.base.BaseFragment;
import com.yyxnb.widget.data.DataConfig;
import com.yyxnb.widget.databinding.FragmentSkinMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.yyxnb.widget.data.DataConfig.SKIN_PATH;


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
//        SkinManager.get().setWindowStatusBarColor(getActivity().getWindow(), R.color.colorPrimary);
//        ViewGroupSetter recyclerViewSetter = new ViewGroupSetter(mRecyclerView);
//        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(mRecyclerView);

//        recyclerViewSetter
//                .childViewBgColor(R.id.mItemLayout, R.attr.colorBackgroundItem)
//                .childViewTextColor(R.id.tvTitle, R.attr.colorTitle)
//                .childViewTextColor(R.id.tvText, R.attr.colorText)
//                .childViewTextColor(R.id.tvHint, R.attr.colorHint);

        // 构建对象
//        SkinTheme theme = new SkinTheme.Builder(getActivity())
//                .backgroundColor(R.id.mLayout, R.attr.colorBackground) // 设置view的背景图片
////                .backgroundColor(R.id.change_btn, R.attr.btn_bg) // 设置按钮的背景色
//                .textColor(R.id.tvTitle, R.attr.colorTitle) // 设置文本颜色
//                .textColor(R.id.tvText, R.attr.colorText) // 设置文本颜色
//                .textColor(R.id.tvHint, R.attr.colorHint) // 设置文本颜色
//                .setter(recyclerViewSetter)           // 手动设置setter
//                .build();

//        SkinManager.get().setViewBackground(binding.mLayout, R.color.colorBackground);
//        SkinManager.get().setTextViewColor(binding.tvTitle, R.color.colorTitle);
//        SkinManager.get().setTextViewColor(binding.tvText, R.color.colorText);
//        SkinManager.get().setTextViewColor(binding.tvHint, R.color.colorHint);

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

        binding.checkBox.setChecked(!SkinManager.get().isUsingDefaultSkin());

        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                SkinManager.get().restoreToDefaultSkin();
                SPUtils.clear(SKIN_PATH);
            } else {
                changeSkin();
            }
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

    @SuppressWarnings("ConstantConditions")
    private void changeSkin() {
        //将assets目录下的皮肤文件拷贝到data/data/.../cache目录下
        String saveDir = getActivity().getCacheDir().getAbsolutePath() + "/skins";
        //将打包生成的apk文件, 重命名为'xxx.skin', 防止apk结尾的文件造成混淆.
        String savefileName = "/night.skin";
        String asset_dir = "skins/night.skin";
        File file = new File(saveDir + File.separator + savefileName);
        if (!file.exists()) {
            AssetFileUtils.copyAssetFile(getActivity(), asset_dir, saveDir, savefileName);
        }
        LogUtils.w(" " + file.getAbsolutePath());
        SPUtils.setParam(SKIN_PATH,file.getAbsolutePath());
        SkinManager.get().loadSkin(file.getAbsolutePath());
//        SkinLoader.getDefault().loadSkin("com.yyxnb.widget","skin_night.apk","night");
    }

}