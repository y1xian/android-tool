package com.yyxnb.oh.core;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.yyxnb.oh.core.callback.ResultCallback;
import com.yyxnb.oh.log.LogUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import cn.hutool.core.thread.ThreadUtil;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/03/10
 * 描    述：UITask，异步处理数据
 * ================================================
 */
public class UITask {

    private static final String TAG = "UITask";
    /**
     * 返回可用处理器的Java虚拟机的数量
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池中至少有2个线程，最多4个线程
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final ExecutorService EXECUTOR_SERVICE = ThreadUtil.newExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE);

    public static <T> void execute(LifecycleOwner owner, final Callable<T> callable, final ResultCallback<T> callback) {
        MediatorLiveData<Object> mediatorLiveData = new MediatorLiveData<>();
        // 操作前，loading
        MutableLiveData<Void> onStarted = new MediatorLiveData<>();
        // 主线程回调
        MutableLiveData<T> onSuccess = new MediatorLiveData<>();
        // 错误回调
        MutableLiveData<Exception> onError = new MediatorLiveData<>();
        mediatorLiveData.addSource(onStarted, o -> callback.onStarted());
        mediatorLiveData.addSource(onSuccess, callback::onSuccess);
        mediatorLiveData.addSource(onError, callback::onError);
        mediatorLiveData.observe(owner, o -> Log.w(TAG, String.format("executeForLiveData: %s", o)));
        run(() -> {
            try {
                onStarted.postValue(null);
                // 异步操作
                T result = callable.call();
                onSuccess.postValue(result);
            } catch (Exception e) {
                LogUtils.e("执行任务异常", e);
                onError.postValue(e);
            }
        });
    }

    /**
     * io,异步任务等适用此线程
     */
    public static Future run(Runnable runnable) {
        return EXECUTOR_SERVICE.submit(runnable);
    }

    public static void post(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    public static ExecutorService getExecutorService() {
        return EXECUTOR_SERVICE;
    }
}
