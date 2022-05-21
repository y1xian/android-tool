package com.yyxnb.android.utils;

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

	private static Handler sMainHandler;

	private LiveDataUtil() {
	}

	public static <T> void setValue(MutableLiveData<T> data, T value) {
		if (data != null) {
			if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
				data.setValue(value);
			} else {
				postSetValue(data, value);
			}
		}
	}

	public static <T> void postSetValue(MutableLiveData<T> data, T value) {
		synchronized (LiveDataUtil.class) {
			if (null == sMainHandler) {
				sMainHandler = new Handler(Looper.getMainLooper());
			}
		}
		sMainHandler.post(LiveDataUtil.a.a(data, value));
	}

	static class a<T> implements Runnable {
		private final MutableLiveData<T> data;
		private final T value;

		public a(MutableLiveData<T> data, T value) {
			this.data = data;
			this.value = value;
		}

		@Override
		public void run() {
			this.data.setValue(this.value);
		}

		public static <T> LiveDataUtil.a<T> a(@NonNull MutableLiveData<T> data, T value) {
			return new LiveDataUtil.a<T>(data, value);
		}
	}

}
