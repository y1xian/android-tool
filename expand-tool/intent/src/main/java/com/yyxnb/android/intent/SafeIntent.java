package com.yyxnb.android.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.yyxnb.android.activity.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * [获取序列化数据时增加保护的Intent]<BR>
 * [推荐使用方法： 1. 覆写Activity getIntent方法，对super结果判空后，返回new SafeIntent(intent) 2. 对外的广播替换为SafeBroadcastReceiver 3.
 * 对外的Service，在onStartCommand里，对intent判空后，封装为SafeIntent]<BR>
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/1
 */
public class SafeIntent extends Intent {
	private static final String EMPTY = "";

	private static final String TAG = SafeIntent.class.getSimpleName();

	/**
	 * [构造简要说明]
	 *
	 * @param intent 原始intent
	 */
	public SafeIntent(Intent intent) {
		super(intent == null ? new Intent() : intent);
	}


	@Override
	public String getAction() {
		try {
			return super.getAction();
		} catch (Throwable e) {
			return "";
		}
	}

	public String getActionReturnNotNull() {
		String result;
		try {
			result = super.getAction();
		} catch (Throwable e) {
			return EMPTY;
		}
		if (result == null) {
			return EMPTY;
		}
		return result;
	}

	@Override
	public Intent setAction(@Nullable String action) {
		try {
			return super.setAction(action);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent setPackage(@Nullable String packageName) {
		try {
			return super.setPackage(packageName);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public void setSelector(@Nullable Intent selector) {
		try {
			super.setSelector(selector);
		} catch (Throwable e) {
			LogUtil.e(TAG, "setSelector: " + e.getMessage(), true);
		}
	}

	@Override
	public String toUri(int flags) {
		try {
			return super.toUri(flags);
		} catch (Throwable e) {
			LogUtil.e(TAG, "toUri: " + e.getMessage(), true);
			return "";
		}
	}

	public String toUriReturnNotNull(int flags) {
		String result = EMPTY;
		try {
			result = super.toUri(flags);
		} catch (Throwable e) {
			LogUtil.e(TAG, "toUri: " + e.getMessage(), true);
		}
		if (result == null) {
			return EMPTY;
		}
		return result;
	}

	@Override
	public <T extends Parcelable> T getParcelableExtra(String name) {
		try {
			return super.getParcelableExtra(name);
		} catch (Throwable e) {
			return null;
		}
	}


	@Override
	public Intent putExtra(String name, Parcelable value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtra(String name, Parcelable[] value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
		try {
			return super.putParcelableArrayListExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtra(String name, Serializable value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public void removeExtra(String name) {
		try {
			super.removeExtra(name);
		} catch (Throwable e) {
			LogUtil.e(TAG, "removeExtra: " + e.getMessage(), true);
		}
	}

	@Override
	public String getStringExtra(String name) {
		try {
			return super.getStringExtra(name);
		} catch (Throwable e) {
			return "";
		}
	}

	public String getStringExtraReturnNotNull(String name) {
		String result;
		try {
			result = super.getStringExtra(name);
		} catch (Throwable e) {
			return EMPTY;
		}
		if (result == null) {
			return EMPTY;
		}
		return result;
	}

	@Override
	public Intent putExtra(String name, int value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtra(String name, String value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtras(Intent src) {
		try {
			return super.putExtras(src);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtra(String name, String[] value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtra(String name, CharSequence value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	@Override
	public Intent putExtra(String name, CharSequence[] value) {
		try {
			return super.putExtra(name, value);
		} catch (Throwable e) {
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBooleanExtra(String name, boolean defaultValue) {
		try {
			return super.getBooleanExtra(name, defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIntExtra(String name, int defaultValue) {
		try {
			return super.getIntExtra(name, defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByteExtra(String name, byte defaultValue) {
		try {
			return super.getByteExtra(name, defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	@Override
	public CharSequence getCharSequenceExtra(String name) {
		try {
			return super.getCharSequenceExtra(name);
		} catch (Throwable e) {
			return "";
		}
	}

	public CharSequence getCharSequenceExtraReturnNotNull(String name) {
		CharSequence result;
		try {
			result = super.getCharSequenceExtra(name);
		} catch (Throwable e) {
			return EMPTY;
		}
		if (result == null) {
			return EMPTY;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char getCharExtra(String name, char defaultValue) {
		try {
			return super.getCharExtra(name, defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}


	@Nullable
	@Override
	public Uri getData() {
		try {
			return super.getData();
		} catch (Throwable e) {
			return null;
		}
	}

	@Deprecated
	public String toURIReturnNotNull() {
		String result = EMPTY;
		try {
			result = super.toURI();
		} catch (Throwable e) {
			LogUtil.e(TAG, "toURI: exception " + e.getMessage(), true);
		}
		if (TextUtils.isEmpty(result)) {
			return EMPTY;
		}
		return result;
	}

	/**
	 * 判断是否有DOS攻击
	 *
	 * @return
	 */
	public boolean hasIntentBomb() {
		boolean hasIntentBomb = false;
		try {
			super.getStringExtra("ANYTHING");
		} catch (Throwable e) {
			hasIntentBomb = true;
		}
		if (hasIntentBomb) {
			LogUtil.e(TAG, "hasIntentBomb");
		}
		return hasIntentBomb;
	}
}
