package com.yyxnb.android.core.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * LiveDataUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/5/17
 */
public class LiveDataUtil {

	private static final Object LOCK = new Object();
	private static Handler sMainHandler;

	private LiveDataUtil() {
	}

	public static <T> void setValue(@NonNull MutableLiveData<T> data, @NonNull T value) {
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			data.setValue(value);
		} else {
			postSetValue(data, value);
		}
	}

	public static <T> void postSetValue(@NonNull MutableLiveData<T> data, @NonNull T value) {
		synchronized (LOCK) {
			if (null == sMainHandler) {
				sMainHandler = new Handler(Looper.getMainLooper());
			}
		}
		sMainHandler.post(InnerRunnable.run(data, value));
	}

	static class InnerRunnable<T> implements Runnable {
		private final MutableLiveData<T> data;
		private final T value;

		public InnerRunnable(MutableLiveData<T> data, T value) {
			this.data = data;
			this.value = value;
		}

		@Override
		public void run() {
			this.data.setValue(this.value);
		}

		public static <T> InnerRunnable<T> run(@NonNull MutableLiveData<T> data, T value) {
			return new InnerRunnable<T>(data, value);
		}
	}

}
