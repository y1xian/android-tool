package com.yyxnb.widget;

import android.support.v4.app.Fragment;

import com.yyxnb.arch.ContainerActivity;
import com.yyxnb.widget.fragments.MainFragment;

public class MainActivity extends ContainerActivity {

    @Override
    public Fragment initBaseFragment() {
        return new MainFragment();
    }

}
