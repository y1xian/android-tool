package com.yyxnb.what.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.io.Serializable;

public class ApplicationManager implements Application.ActivityLifecycleCallbacks, Serializable {

    private static volatile ApplicationManager mInstance = null;

    private ApplicationManager() {
    }

    public static ApplicationManager getInstance() {
        if (null == mInstance) {
            synchronized (ApplicationManager.class) {
                if (null == mInstance) {
                    mInstance = new ApplicationManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 避免序列化破坏单例模式
     */
    private Object readResolve() {
        return mInstance;
    }

    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(this);
    }

    public void unInit(Application app) {
        app.unregisterActivityLifecycleCallbacks(this);
    }

    // === 生命周期

    private ApplicationUtils.ActivityLifecycleCallbacks lifecycleCallbacks = new ApplicationUtils.ActivityLifecycleCallbacks();

    public void setLifecycleCallbacks(ApplicationUtils.ActivityLifecycleCallbacks lifecycleCallbacks) {
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
