package com.yyxnb.oh.arch.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.LruCache;

import androidx.fragment.app.FragmentActivity;

import com.yyxnb.oh.application.ApplicationUtils;
import com.yyxnb.oh.arch.helper.ActivityHelper;

/**
 * ================================================
 * 作    者：yyx
 * 版    本：1.0
 * 日    期：2020/11/21
 * 历    史：
 * 描    述：Activity 注册监听生命周期
 * ================================================
 */
public class ActivityLifecycle extends ApplicationUtils.ActivityLifecycleCallbacks {

    private static volatile ActivityLifecycle mInstance = null;

    private ActivityLifecycle() {
    }

    public static ActivityLifecycle getInstance() {
        if (null == mInstance) {
            synchronized (ActivityLifecycle.class) {
                if (null == mInstance) {
                    mInstance = new ActivityLifecycle();
                }
            }
        }
        return mInstance;
    }

    private final LruCache<String, IActivityLifecycle> cache = new LruCache<>(100);

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityHelper.getInstance().addActivity(activity);
        IActivityLifecycle activityDelegate;

        if (fetchActivityDelegate(activity) != null) {
            activityDelegate = fetchActivityDelegate(activity);
        } else {
            activityDelegate = newDelegate(activity);
        }

        cache.put(getKey(activity), activityDelegate);

        activityDelegate.onCreate(savedInstanceState);
        registerFragmentCallback(activity);
    }

    private void registerFragmentCallback(Activity activity) {
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager().
                    registerFragmentLifecycleCallbacks(FragmentLifecycle.getInstance(), true);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (fetchActivityDelegate(activity) != null) {
            fetchActivityDelegate(activity).onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (fetchActivityDelegate(activity) != null) {
            fetchActivityDelegate(activity).onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (fetchActivityDelegate(activity) != null) {
            fetchActivityDelegate(activity).onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (fetchActivityDelegate(activity) != null) {
            fetchActivityDelegate(activity).onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (fetchActivityDelegate(activity) != null) {
            fetchActivityDelegate(activity).onSaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (fetchActivityDelegate(activity) != null) {
            fetchActivityDelegate(activity).onDestroy();
        }
        if (null != activity) {
            ActivityHelper.getInstance().removeActivity(activity);
            cache.remove(getKey(activity));
        }
    }

    private IActivityLifecycle fetchActivityDelegate(Activity activity) {
        return cache.get(getKey(activity));
    }

    private IActivityLifecycle newDelegate(Activity activity) {

        return new ActivityLifecycleImpl((FragmentActivity) activity);
    }

    private String getKey(Activity activity) {
        return activity.getClass().getName() + activity.hashCode();
    }
}
