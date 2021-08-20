package com.yyxnb.what.arch.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yyxnb.what.arch.helper.ActivityHelper;

/**
 * ================================================
 * 作    者：yyx
 * 版    本：1.0
 * 日    期：2020/11/21
 * 历    史：
 * 描    述：Fragment 注册生命周期监听
 * ================================================
 */
public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

    private static volatile FragmentLifecycle mInstance = null;

    private FragmentLifecycle() {
    }

    public static FragmentLifecycle getInstance() {
        if (null == mInstance) {
            synchronized (FragmentLifecycle.class) {
                if (null == mInstance) {
                    mInstance = new FragmentLifecycle();
                }
            }
        }
        return mInstance;
    }

    private final LruCache<String, IFragmentLifecycle> cache = new LruCache<>(100);

    @Override
    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        ActivityHelper.getInstance().addFragment(f);
        IFragmentLifecycle fragmentDelegate = fetchFragmentDelegate(f, fm);
        fragmentDelegate.onAttached(context);
    }

    @Override
    public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onCreated(savedInstanceState);
        }
    }

    @Override
    public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onViewCreated(v, savedInstanceState);
        }
    }

    @Override
    public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onStarted();
        }
    }

    @Override
    public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onResumed();
        }
    }

    @Override
    public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onPaused();
        }
    }

    @Override
    public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onStopped();
        }
    }

    @Override
    public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onSaveInstanceState(outState);
        }
    }

    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onViewDestroyed();
        }
    }

    @Override
    public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        ActivityHelper.getInstance().removeFragment(f);
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onDestroyed();
        }
    }

    @Override
    public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (fetchFragmentDelegateFromCache(f) != null) {
            fetchFragmentDelegateFromCache(f).onDetached();
        }
        cache.remove(getKey(f));
    }

    private IFragmentLifecycle fetchFragmentDelegate(Fragment fragment, FragmentManager manager) {

        IFragmentLifecycle fragmentDelegate;

        if (fetchFragmentDelegateFromCache(fragment) != null) {
            fragmentDelegate = fetchFragmentDelegateFromCache(fragment);
        } else {
            fragmentDelegate = newDelegate(manager, fragment);
        }
        cache.put(getKey(fragment), fragmentDelegate);
        return fragmentDelegate;
    }

    private IFragmentLifecycle fetchFragmentDelegateFromCache(Fragment fragment) {
        return cache.get(getKey(fragment));
    }

    private IFragmentLifecycle newDelegate(FragmentManager manager, Fragment fragment) {
        return new FragmentLifecycleImpl(fragment, manager);
    }

    private String getKey(Fragment fragment) {
        return fragment.getClass().getName() + fragment.hashCode();
    }

}
