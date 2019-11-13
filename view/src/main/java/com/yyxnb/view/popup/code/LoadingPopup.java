package com.yyxnb.view.popup.code;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yyxnb.view.R;


/**
 * Description: 加载对话框
 */
public class LoadingPopup extends CenterPopup {

    private TextView tvTitle;

    public LoadingPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int initLayoutResId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout._popup_center_impl_loading;
    }

    /**
     * 绑定已有布局
     *
     * @param layoutId 如果要显示标题，则要求必须有id为tv_title的TextView，否则无任何要求
     * @return
     */
    public LoadingPopup bindLayout(int layoutId) {
        bindLayoutId = layoutId;
        return this;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tvTitle = findViewById(R.id.tv_title);
        setup();
    }

    protected void setup() {
        if (title != null && tvTitle != null) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
    }

    private String title;

    public LoadingPopup setTitle(String title) {
        this.title = title;
        setup();
        return this;
    }
}
