package com.yyxnb.android.core.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * ViewModelScopeUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/18
 */
public class ViewModelScopeUtil {
	private static ViewModelProvider mFragmentProvider;
	private static ViewModelProvider mActivityProvider;
	private static ViewModelProvider mApplicationProvider;

	private ViewModelScopeUtil() {
	}

	public static <T extends ViewModel> T getFragmentScopeViewModel(
			@NonNull Fragment fragment,
			@NonNull Class<T> modelClass) {
		if (mFragmentProvider == null) {
			mFragmentProvider = new ViewModelProvider(fragment);
		}
		return mFragmentProvider.get(modelClass);
	}

	public static <T extends ViewModel> T getActivityScopeViewModel(
			@NonNull AppCompatActivity activity,
			@NonNull Class<T> modelClass) {
		if (mActivityProvider == null) {
			mActivityProvider = new ViewModelProvider(activity);
		}
		return mActivityProvider.get(modelClass);
	}

	public static <T extends ViewModel> T getApplicationScopeViewModel(
			@NonNull Class<T> modelClass) {
		if (mApplicationProvider == null) {
			mApplicationProvider = new ViewModelProvider(ApplicationInstance.getInstance());
		}
		return mApplicationProvider.get(modelClass);
	}

	static class ApplicationInstance implements ViewModelStoreOwner {
		private final static ApplicationInstance S_INSTANCE = new ApplicationInstance();
		private ViewModelStore mAppViewModelStore;

		private ApplicationInstance() {
		}

		public static ApplicationInstance getInstance() {
			return S_INSTANCE;
		}

		@NonNull
		@Override
		public ViewModelStore getViewModelStore() {
			if (mAppViewModelStore == null) {
				mAppViewModelStore = new ViewModelStore();
			}
			return mAppViewModelStore;
		}
	}
}