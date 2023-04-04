package com.yyxnb.android.skin.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.core.view.ViewCompat;

import com.yyxnb.android.skin.SkinManager;
import com.yyxnb.android.skin.attr.SkinAttr;
import com.yyxnb.android.skin.attr.SkinAttrHolder;
import com.yyxnb.android.skin.callback.OnSkinChangeCallback;
import com.yyxnb.android.utils.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于拦截View的创建
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinCompatViewInflater {

	private static final String TAG = SkinCompatViewInflater.class.getSimpleName();

	private static final Class<?>[] S_CONSTRUCTOR_SIGNATURE = new Class[]{
			Context.class, AttributeSet.class};
	private static final int[] S_ON_CLICK_ATTRS = new int[]{android.R.attr.onClick};

	private static final String[] S_CLASS_PREFIX_LIST = {
			"android.widget.",
			"android.view.",
			"android.webkit."
	};

	private static final Map<String, Constructor<? extends View>> STRING_CONSTRUCTOR_MAP
			= new ArrayMap<>();

	private final Object[] mConstructorArgs = new Object[2];

	@SuppressLint("RestrictedApi")
	public final View createView(View parent, final String name, @NonNull Context context,
								 @NonNull AttributeSet attrs, boolean inheritContext,
								 boolean readAndroidTheme, boolean readAppTheme, boolean wrapContext, OnSkinChangeCallback callback) {

		// We can emulate Lollipop's android:theme attribute propagating down the view hierarchy
		// by using the parent's context
		if (inheritContext && parent != null) {
			context = parent.getContext();
		}
		if (readAndroidTheme || readAppTheme) {
			// We then apply the theme on the context, if specified
			context = themifyContext(context, attrs, readAndroidTheme, readAppTheme);
		}
		if (wrapContext) {
			context = TintContextWrapper.wrap(context);
		}

		View view = null;

		// We need to 'inject' our tint aware Views in place of the standard framework versions
		switch (name) {
			case "TextView":
				view = new AppCompatTextView(context, attrs);
				break;
			case "ImageView":
				view = new AppCompatImageView(context, attrs);
				break;
			case "Button":
				view = new AppCompatButton(context, attrs);
				break;
			case "EditText":
				view = new AppCompatEditText(context, attrs);
				break;
			case "Spinner":
				view = new AppCompatSpinner(context, attrs);
				break;
			case "ImageButton":
				view = new AppCompatImageButton(context, attrs);
				break;
			case "CheckBox":
				view = new AppCompatCheckBox(context, attrs);
				break;
			case "RadioButton":
				view = new AppCompatRadioButton(context, attrs);
				break;
			case "CheckedTextView":
				view = new AppCompatCheckedTextView(context, attrs);
				break;
			case "AutoCompleteTextView":
				view = new AppCompatAutoCompleteTextView(context, attrs);
				break;
			case "MultiAutoCompleteTextView":
				view = new AppCompatMultiAutoCompleteTextView(context, attrs);
				break;
			case "RatingBar":
				view = new AppCompatRatingBar(context, attrs);
				break;
			case "SeekBar":
				view = new AppCompatSeekBar(context, attrs);
				break;
			default:
				break;
		}

		List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttr(context, attrs);
		if (skinAttrList.isEmpty()) {
			return view;
		}

		if (view == null) {
			// If the original context does not equal our themed context, then we need to manually
			// inflate it using the name so that android:theme takes effect.
			view = createViewFromTag(context, name, attrs);
		}

		if (view != null) {
			// If we have created a view, check its android:onClick
			checkOnClickListener(view, attrs);
		}

		if (callback != null && skinAttrList.size() != 0) {
			List<SkinAttrHolder> skinAttrHolders = SkinManager.getInstance().getSkinAttrHolders(callback);
			if (skinAttrHolders == null) {
				skinAttrHolders = new ArrayList<>();
				// 注册
				SkinManager.getInstance().register(callback, skinAttrHolders);
			}
			SkinAttrHolder skinAttrHolder = new SkinAttrHolder(view, skinAttrList);
			skinAttrHolders.add(skinAttrHolder);
			SkinManager.getInstance().checkSkin(callback, view, skinAttrHolder);
		}
		return view;
	}

	private View createViewFromTag(Context context, String name, AttributeSet attrs) {
		try {
			mConstructorArgs[0] = context;
			mConstructorArgs[1] = attrs;
			if (-1 == name.indexOf('.')) {
				if ("view".equals(name)) {
					name = attrs.getAttributeValue(null, "class");
				}
				for (String s : S_CLASS_PREFIX_LIST) {
					final View view = createView(context, name, s);
					if (view != null) {
						return view;
					}
				}
				return null;
			} else {
				return createView(context, name, null);
			}
		} catch (Exception e) {
			// We do not want to catch these, lets return null and let the actual LayoutInflater
			// try
			return null;
		} finally {
			// Don't retain references on context.
			mConstructorArgs[0] = null;
			mConstructorArgs[1] = null;
		}
	}

	/**
	 * android:onClick doesn't handle views with a ContextWrapper context. This method
	 * backports new framework functionality to traverse the Context wrappers to find a
	 * suitable target.
	 */
	private void checkOnClickListener(View view, AttributeSet attrs) {
		final Context context = view.getContext();

		if (!(context instanceof ContextWrapper) ||
				(Build.VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view))) {
			// Skip our compat functionality if: the Context isn't a ContextWrapper, or
			// the view doesn't have an OnClickListener (we can only rely on this on API 15+ so
			// always use our compat code on older devices)
			return;
		}

		final TypedArray a = context.obtainStyledAttributes(attrs, S_ON_CLICK_ATTRS);
		final String handlerName = a.getString(0);
		if (handlerName != null) {
			view.setOnClickListener(new DeclaredOnClickListener(view, handlerName));
		}
		a.recycle();
	}

	private View createView(Context context, String name, String prefix)
			throws InflateException {
		Constructor<? extends View> constructor = STRING_CONSTRUCTOR_MAP.get(name);

		try {
			if (constructor == null) {
				// Class not found in the cache, see if it's real, and try to add it
				Class<? extends View> clazz = context.getClassLoader().loadClass(
						prefix != null ? (prefix + name) : name).asSubclass(View.class);

				constructor = clazz.getConstructor(S_CONSTRUCTOR_SIGNATURE);
				STRING_CONSTRUCTOR_MAP.put(name, constructor);
			}
			constructor.setAccessible(true);
			return constructor.newInstance(mConstructorArgs);
		} catch (Exception e) {
			// We do not want to catch these, lets return null and let the actual LayoutInflater
			// try
			return null;
		}
	}

	/**
	 * Allows us to emulate the {@code android:theme} attribute for devices before L.
	 */
	private static Context themifyContext(Context context, AttributeSet attrs,
										  boolean useAndroidTheme, boolean useAppTheme) {
		final TypedArray a = context.obtainStyledAttributes(attrs, androidx.appcompat.R.styleable.View, 0, 0);
		int themeId = 0;
		if (useAndroidTheme) {
			// First try reading android:theme if enabled
			themeId = a.getResourceId(androidx.appcompat.R.styleable.View_android_theme, 0);
		}
		if (useAppTheme && themeId == 0) {
			// ...if that didn't work, try reading app:theme (for legacy reasons) if enabled
			themeId = a.getResourceId(androidx.appcompat.R.styleable.View_theme, 0);

			if (themeId != 0) {
				LogUtil.i(TAG, "app:theme is now deprecated. "
						+ "Please move to using android:theme instead.");
			}
		}
		a.recycle();

		if (themeId != 0 && (!(context instanceof ContextThemeWrapper)
				|| ((ContextThemeWrapper) context).getThemeResId() != themeId)) {
			// If the context isn't a ContextThemeWrapper, or it is but does not have
			// the same theme as we need, wrap it in a new wrapper
			context = new ContextThemeWrapper(context, themeId);
		}
		return context;
	}

	/**
	 * An implementation of OnClickListener that attempts to lazily load a
	 * named click handling method from a parent or ancestor context.
	 */
	private static class DeclaredOnClickListener implements View.OnClickListener {
		private final View mHostView;
		private final String mMethodName;

		private Method mResolvedMethod;
		private Context mResolvedContext;

		public DeclaredOnClickListener(@NonNull View hostView, @NonNull String methodName) {
			mHostView = hostView;
			mMethodName = methodName;
		}

		@Override
		public void onClick(@NonNull View v) {
			if (mResolvedMethod == null) {
				resolveMethod(mHostView.getContext(), mMethodName);
			}

			try {
				mResolvedMethod.invoke(mResolvedContext, v);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(
						"Could not execute non-public method for android:onClick", e);
			} catch (InvocationTargetException e) {
				throw new IllegalStateException(
						"Could not execute method for android:onClick", e);
			}
		}

		@NonNull
		private void resolveMethod(@Nullable Context context, @NonNull String name) {
			while (context != null) {
				try {
					if (!context.isRestricted()) {
						final Method method = context.getClass().getMethod(mMethodName, View.class);
						if (method != null) {
							mResolvedMethod = method;
							mResolvedContext = context;
							return;
						}
					}
				} catch (NoSuchMethodException e) {
					// Failed to find method, keep searching up the hierarchy.
				}

				if (context instanceof ContextWrapper) {
					context = ((ContextWrapper) context).getBaseContext();
				} else {
					// Can't search up the hierarchy, null out and fail.
					context = null;
				}
			}

			final int id = mHostView.getId();
			final String idText = id == View.NO_ID ? "" : " with id '"
					+ mHostView.getContext().getResources().getResourceEntryName(id) + "'";
			throw new IllegalStateException("Could not find method " + mMethodName
					+ "(View) in a parent or ancestor Context for android:onClick "
					+ "attribute defined on view " + mHostView.getClass() + idText);
		}
	}
}
