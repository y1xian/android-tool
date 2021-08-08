package com.yyxnb.oh.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.yyxnb.oh.app.AppUtils;

import java.io.Serializable;

/**
 * app 管理
 *
 * @author yyx
 */
public final class CoreManager implements Application.ActivityLifecycleCallbacks, Serializable {

    @SuppressLint("StaticFieldLeak")
    private volatile static CoreManager coreManager;

    private CoreManager() {
    }

    public static CoreManager getInstance() {
        if (coreManager == null) {
            synchronized (CoreManager.class) {
                if (coreManager == null) {
                    coreManager = new CoreManager();
                }
            }
        }
        return coreManager;
    }

    /**
     * 避免序列化破坏单例模式
     */
    private Object readResolve() {
        return coreManager;
    }

    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(this);
    }

    public void unInit(Application app) {
        app.unregisterActivityLifecycleCallbacks(this);
    }


    // === 生命周期

    private AppUtils.ActivityLifecycleCallbacks lifecycleCallbacks = new AppUtils.ActivityLifecycleCallbacks();

    public void setLifecycleCallbacks(AppUtils.ActivityLifecycleCallbacks lifecycleCallbacks) {
        this.lifecycleCallbacks = lifecycleCallbacks;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        lifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        lifecycleCallbacks.onActivityStarted(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        lifecycleCallbacks.onActivityResumed(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        lifecycleCallbacks.onActivityPaused(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        lifecycleCallbacks.onActivityStopped(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        lifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        lifecycleCallbacks.onActivityDestroyed(activity);
    }

}
