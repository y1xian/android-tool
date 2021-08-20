package com.yyxnb.what.arch.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/05/12
 * 描    述：Fragment 代理
 * ================================================
 */
public class FragmentPageDelegate extends PageDelegate {

    private static final String BUNDLE_KEY_FRAGMENT_STATUS_HIDE = "/bundle/key/fragment/status/hide";

    private FragmentActivity mActivity;
    private final Fragment mFragment;
    private Bundle mForResultTarget;

    FragmentPageDelegate(@NonNull Fragment fragment) {
        super(fragment);
        this.mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initResultParams(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Init the params of {@link #startFragmentForResult(Fragment, int)}
     */
    private void initResultParams(Bundle savedInstanceState) {
        Bundle bundle = savedInstanceState == null ? mFragment.getArguments() : savedInstanceState;
        if (bundle == null) {
            bundle = new Bundle();
        }
        mForResultTarget = bundle.getParcelable(BUNDLE_KEY_FOR_RESULT);
    }


    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE, mFragment.isHidden());
        outState.putParcelable(BUNDLE_KEY_FOR_RESULT, mForResultTarget);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean isResumed() {
        return mFragment.isResumed();
    }


    @Override
    public void setResult(int resultCode, Intent result) {
    }

}
