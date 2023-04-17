package com.yyxnb.android.page;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.yyxnb.android.activity.SafeAppCompatActivity;
import com.yyxnb.android.core.utils.BarUtil;
import com.yyxnb.android.intent.SafeIntent;
import com.yyxnb.android.skin.SkinResource;
import com.yyxnb.android.skin.callback.OnSkinChangeCallback;
import com.yyxnb.android.skin.support.SkinCompat;
import com.yyxnb.android.utils.LogUtil;

/**
 * Description: 盛装Fragment的一个容器(代理)Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 *
 * @author : yyx
 * @date ：2018/6/9
 */
public class ContainerActivity extends SafeAppCompatActivity implements OnSkinChangeCallback {

	private static final String TAG = ContainerActivity.class.getSimpleName();

	private SkinCompat mSkinCompat;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		mSkinCompat = new SkinCompat(this, this);
		super.onCreate(savedInstanceState);
		BarUtil.setStatusBarTranslucent(getWindow(), true, false);
		BarUtil.setStatusBarStyle(getWindow(), true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		final FrameLayout mFrameLayout = new FrameLayout(this);
		mFrameLayout.setId(android.R.id.content);
		setContentView(mFrameLayout);
		initView();
	}

	@Override
	public View onCreateView(View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
		View view = mSkinCompat.createView(parent, name, context, attrs);
		if (view == null) {
			return super.onCreateView(parent, name, context, attrs);
		}
		return view;
	}

	protected Fragment initBaseFragment() {
		return null;
	}

	public void initView() {
		try {
			SafeIntent intent = new SafeIntent(getIntent());
			final Fragment mFragment = initBaseFragment();
			if (mFragment != null) {
				if (intent.getBundleExtra(ArgumentKeys.BUNDLE) != null) {
					mFragment.setArguments(intent.getBundleExtra(ArgumentKeys.BUNDLE));
				}
				setRootFragment(mFragment, android.R.id.content);
				return;
			}

			String fragmentName = intent.getStringExtra(ArgumentKeys.FRAGMENT);
			if (fragmentName.isEmpty()) {
				throw new IllegalArgumentException("can not find page fragmentName");
			}
			Class<?> fragmentClass = Class.forName(fragmentName);
			Fragment fragment = (Fragment) fragmentClass.newInstance();
			if (intent.getBundleExtra(ArgumentKeys.BUNDLE) != null) {
				fragment.setArguments(intent.getBundleExtra(ArgumentKeys.BUNDLE));
			}
			setRootFragment(fragment, android.R.id.content);
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		// 释放内存
		mSkinCompat.release(this);
		mSkinCompat = null;
		super.onDestroy();
	}

	/**
	 * 换肤的回调,用于扩展自定义View的换肤功能
	 *
	 * @param view         view
	 * @param skinResource skinResource
	 */
	@Override
	public void onSkinChange(View view, SkinResource skinResource) {
	}

	public <T extends Fragment> void setRootFragment(T fragment, int containerId) {
		try {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(containerId, fragment, String.valueOf(fragment.hashCode()));
			transaction.addToBackStack(String.valueOf(fragment.hashCode()));
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}
}
