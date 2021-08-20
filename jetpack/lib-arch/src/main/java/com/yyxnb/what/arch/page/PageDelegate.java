package com.yyxnb.what.arch.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.yyxnb.what.arch.config.ArchManager;
import com.yyxnb.what.arch.config.ArchOption;
import com.yyxnb.what.arch.interfaces.IPage;
import com.yyxnb.what.log.LogUtils;

public abstract class PageDelegate implements IPage {

    static final String FRAGMENT = "FRAGMENT";
    static final String BUNDLE_KEY_FOR_RESULT = "/bundle/key/for/result";
    static final String BUNDLE_KEY_FOR_RESULT_RECEIVE = BUNDLE_KEY_FOR_RESULT + 1;
    static final String BUNDLE_KEY_FOR_RESULT_REQUEST_CODE = BUNDLE_KEY_FOR_RESULT + 2;

    private final Object mPageTarget;
    protected Context mContext;
    protected final static ArchOption OPTION = ArchManager.getInstance().getConfig();

    public static PageDelegate create(@NonNull Object object) {
        if (object instanceof FragmentActivity) {
            return new ActivityPageDelegate((FragmentActivity) object);
        } else if (object instanceof Fragment) {
            return new FragmentPageDelegate((Fragment) object);
        } else {
            throw new PageException(
                    "Annotation class can only used on Activity or Fragment");
        }
    }

    public PageDelegate(Object pageTarget) {
        this.mPageTarget = pageTarget;
    }

    @NonNull
    @Override
    public Object getPageHost() {
        return mPageTarget;
    }

    // ----------------------------------------------------------------------- 生命周期

    public void onAttach(Context context) {
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    }

    public abstract void onResume();

    public void onPause() {
    }

    public abstract void onSaveInstanceState(Bundle outState);

    /**
     * Fragment 销毁前调用
     */
    public abstract void onDestroy();


    // -----------------------------------------------------------------------

    @Override
    public void startFragment(@NonNull Fragment targetFragment) {
        startFragmentForResult(targetFragment, -1);
    }

    @Override
    public void startFragmentForResult(@NonNull Fragment targetFragment, int requestCode) {
        Intent intent = new Intent();
        ComponentName componentName =
                new ComponentName(
                        mContext.getPackageName(),
                        OPTION.getContainerActivityClassName()
                );
        intent.setComponent(componentName);
        intent.putExtra(FRAGMENT, targetFragment.getClass().getCanonicalName());
        if (mPageTarget instanceof FragmentActivity) {
            ((FragmentActivity) mPageTarget).startActivityForResult(intent, requestCode);
        } else if (mPageTarget instanceof Fragment) {
            ((Fragment) mPageTarget).startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void replaceFragment(@NonNull String fragmentName, @IdRes int containerViewId) {
        try {
            Class<?> fragmentClass = Class.forName(fragmentName);
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            replaceFragment(fragment, containerViewId);
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {

        FragmentTransaction transaction = null;
        if (mPageTarget instanceof FragmentActivity) {
            transaction = ((FragmentActivity) mPageTarget).getSupportFragmentManager().beginTransaction();
        } else if (mPageTarget instanceof Fragment) {
            transaction = ((Fragment) mPageTarget).getChildFragmentManager().beginTransaction();
        }
        assert transaction != null;
        transaction.replace(containerViewId, fragment, fragment.getTag());
        transaction.addToBackStack(fragment.getTag());
        transaction.commitAllowingStateLoss();
    }

    /**
     * Throw the exception.
     */
    void throwException(PageException e) {
        throw e;
    }
}
