package com.yyxnb.oh.arch.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.yyxnb.oh.core.interfaces.ILifecycle;

/**
 * ================================================
 * 作    者：yyx
 * 版    本：1.0
 * 日    期：2020/11/21
 * 历    史：
 * 描    述：IFragmentDelegate
 * ================================================
 */
public interface IFragmentLifecycle extends ILifecycle {

    void onAttached(Context context);

    void onCreated(Bundle savedInstanceState);

    void onViewCreated(View view, Bundle savedInstanceState);

    void onActivityCreated(Bundle savedInstanceState);

    void onStarted();

    void onResumed();

    void onPaused();

    void onStopped();

    void onSaveInstanceState(Bundle outState);

    void onViewDestroyed();

    void onDestroyed();

    void onDetached();

    boolean isAdd();

}
