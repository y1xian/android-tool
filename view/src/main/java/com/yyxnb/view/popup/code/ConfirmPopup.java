package com.yyxnb.view.popup.code;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yyxnb.view.R;
import com.yyxnb.view.popup.interfaces.OnCancelListener;
import com.yyxnb.view.popup.interfaces.OnConfirmListener;
import com.yyxnb.view.popup.Popup;


/**
 * Description: 确定和取消的对话框
 */
public class ConfirmPopup extends CenterPopup implements View.OnClickListener {
    OnCancelListener cancelListener;
    OnConfirmListener confirmListener;
    TextView tvTitle, tvContent, tvCancel, tvConfirm;
    String title, content, hint, cancelText, confirmText;
    boolean isHideCancel = false;

    public ConfirmPopup(@NonNull Context context) {
        super(context);
    }

    /**
     * 绑定已有布局
     *
     * @param layoutId 要求布局中必须包含的TextView以及id有：tvTitle，tvContent，tvCancel，tvConfirm
     * @return
     */
    public ConfirmPopup bindLayout(int layoutId) {
        bindLayoutId = layoutId;
        return this;
    }

    @Override
    protected int initLayoutResId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout._popup_center_impl_confirm;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);

        if (bindLayoutId == 0) {
            applyPrimaryColor();
        }

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        } else {
            tvContent.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tvCancel.setText(cancelText);
        }
        if (!TextUtils.isEmpty(confirmText)) {
            tvConfirm.setText(confirmText);
        }
        if (isHideCancel) {
            tvCancel.setVisibility(GONE);
        }
    }

    protected void applyPrimaryColor() {
        tvCancel.setTextColor(Popup.getPrimaryColor());
        tvConfirm.setTextColor(Popup.getPrimaryColor());
    }

    public ConfirmPopup setListener(OnConfirmListener confirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
        return this;
    }

    public ConfirmPopup setTitleContent(String title, String content, String hint) {
        this.title = title;
        this.content = content;
        this.hint = hint;
        return this;
    }

    public ConfirmPopup setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public ConfirmPopup setConfirmText(String confirmText) {
        this.confirmText = confirmText;
        return this;
    }

    public ConfirmPopup hideCancelBtn() {
        isHideCancel = true;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tvCancel) {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        } else if (v == tvConfirm) {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            if (popupInfo.autoDismiss) {
                dismiss();
            }
        }
    }
}
