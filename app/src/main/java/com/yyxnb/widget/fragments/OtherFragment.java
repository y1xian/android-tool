package com.yyxnb.widget.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyxnb.widget.R;
import com.yyxnb.widget.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends BaseFragment {


    public OtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other, container, false);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
}
