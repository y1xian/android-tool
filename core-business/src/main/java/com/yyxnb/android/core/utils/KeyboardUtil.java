package com.yyxnb.android.core.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

/**
 * KeyboardUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class KeyboardUtil {

	private KeyboardUtil() {
	}

	public static void showSoftInput() {
		InputMethodManager imm = (InputMethodManager) UtilInner.getApp()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void hideSoftInput(@NonNull final View view) {
		InputMethodManager imm =
				(InputMethodManager) UtilInner.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void toggleSoftInput() {
		InputMethodManager imm =
				(InputMethodManager) UtilInner.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}
		imm.toggleSoftInput(0, 0);
	}

	public static void fixSoftInputLeaks(@NonNull final Activity activity) {
		fixSoftInputLeaks(activity.getWindow());
	}

	public static void fixSoftInputLeaks(@NonNull final Window window) {
		InputMethodManager imm =
				(InputMethodManager) UtilInner.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}
		String[] leakViews =
				new String[]{"mLastSrvView", "mCurRootView", "mServedView", "mNextServedView"};
		for (String leakView : leakViews) {
			try {
				Field leakViewField = InputMethodManager.class.getDeclaredField(leakView);
				if (!leakViewField.isAccessible()) {
					leakViewField.setAccessible(true);
				}
				Object obj = leakViewField.get(imm);
				if (!(obj instanceof View)) {
					continue;
				}
				View view = (View) obj;
				if (view.getRootView() == window.getDecorView().getRootView()) {
					leakViewField.set(imm, null);
				}
			} catch (Throwable ignore) {/**/}
		}
	}
}
