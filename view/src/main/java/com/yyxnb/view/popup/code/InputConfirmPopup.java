package com.yyxnb.view.popup.code;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yyxnb.view.R;
import com.yyxnb.utils.interfaces.OnCancelListener;
import com.yyxnb.view.popup.interfaces.OnInputConfirmListener;
import com.yyxnb.view.popup.Popup;
import com.yyxnb.view.popup.PopupUtils;


/**
 * Description: 带输入框，确定和取消的对话框
 */
public class InputConfirmPopup extends ConfirmPopup implements View.OnClickListener {

    public InputConfirmPopup(@NonNull Context context) {
        super(context);
    }

    /**
     * 绑定已有布局
     *
     * @param layoutId 在Confirm弹窗基础上需要增加一个id为et_input的EditText
     * @return
     */
    @Override
    public InputConfirmPopup bindLayout(int layoutId) {
        bindLayoutId = layoutId;
        return this;
    }

    EditText etInput;
    public String inputContent;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        etInput = findViewById(R.id.etInput);
        etInput.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(hint)) {
            etInput.setHint(hint);
        }
        if (!TextUtils.isEmpty(inputContent)) {
            etInput.setText(inputContent);
            etInput.setSelection(inputContent.length());
        }
        applyPrimary();
    }

    public EditText getEditText() {
        return etInput;
    }

    protected void applyPrimary() {
        super.applyPrimaryColor();
        PopupUtils.setCursorDrawableColor(etInput, Popup.getPrimaryColor());
        etInput.post(() -> {
            BitmapDrawable defaultDrawable = PopupUtils.createBitmapDrawable(getResources(), etInput.getMeasuredWidth(), Color.parseColor("#888888"));
            BitmapDrawable focusDrawable = PopupUtils.createBitmapDrawable(getResources(), etInput.getMeasuredWidth(), Popup.getPrimaryColor());
            etInput.setBackgroundDrawable(PopupUtils.createSelector(defaultDrawable, focusDrawable));
        });

    }

    OnCancelListener cancelListener;
    OnInputConfirmListener inputConfirmListener;

    public void setListener(OnInputConfirmListener inputConfirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.inputConfirmListener = inputConfirmListener;
    }

    @Override
    public void onClick(View v) {
        if (v == tvCancel) {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        } else if (v == tvConfirm) {
            if (inputConfirmListener != null) {
                inputConfirmListener.onConfirm(etInput.getText().toString().trim());
            }
            if (popupInfo.autoDismiss) {
                dismiss();
            }
        }
    }
}
