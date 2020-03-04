package com.yyxnb.widget.fragments.lazy;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.yyxnb.arch.annotations.BindFragment;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.arch.utils.FragmentManagerUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.Nullable;

/**
 * startActivityForResult .
 */
@BindFragment(layoutRes = R.layout.fragment_for_result, group = 3, statusBarColor = Color.BLUE)
public class ForResultFragment extends BaseFragment {

    private EditText etInput;
    private TextView tvConfirm;

    public static ForResultFragment newInstance() {

        Bundle args = new Bundle();

        ForResultFragment fragment = new ForResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        LogUtils.INSTANCE.w("--" + getArguments().getString("x"));

        etInput = findViewById(R.id.etInput);
        tvConfirm = findViewById(R.id.tvConfirm);

        etInput.setText(getArguments().getString("x"));

        tvConfirm.setOnClickListener(v -> {
            String str = etInput.getText().toString();
            if (TextUtils.isEmpty(str)) {
                str = "666";
            }
            Intent intent = new Intent();
            intent.putExtra("data", str);
            setResult(321, intent);
            LogUtils.INSTANCE.w("setResult");
            finishAll(4);
        });
    }

    @Override
    public void initViewData() {
        super.initViewData();

        LogUtils.INSTANCE.w("" + FragmentManagerUtils.INSTANCE.currentFragment().getClass().getSimpleName());

    }

}
