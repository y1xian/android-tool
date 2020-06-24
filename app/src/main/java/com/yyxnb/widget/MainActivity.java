package com.yyxnb.widget;

import android.support.v4.app.Fragment;

import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.widget.base.ContainerActivity;
import com.yyxnb.widget.fragments.MainFragment;

@BindRes
public class MainActivity extends ContainerActivity {

    @Override
    public Fragment initBaseFragment() {
        return new MainFragment();
    }

}
