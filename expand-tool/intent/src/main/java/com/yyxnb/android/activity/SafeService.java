package com.yyxnb.android.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;

import com.yyxnb.android.intent.IntentUtils;
import com.yyxnb.android.intent.SafeIntent;
import com.yyxnb.android.utils.LogUtil;

/**
 * SafeService
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/1
 */
public abstract class SafeService extends Service {
	private static final String TAG = SafeService.class.getSimpleName();

	@Override
	public void onCreate() {
		try {
			super.onCreate();
		} catch (Exception e) {
			LogUtil.e(TAG, "onCreate: " + e.getMessage(), true);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (IntentUtils.hasIntentBomb(intent)) {
			LogUtil.e(TAG, "onStartCommand : hasIntentBomb");
		}
		try {
			return super.onStartCommand(new SafeIntent(intent), flags, startId);
		} catch (Exception e) {
			LogUtil.e(TAG, "onStartCommand: " + e.getMessage(), true);
		}
		return START_STICKY_COMPATIBILITY;
	}

	@Deprecated
	@Override
	public void onStart(Intent intent, int startId) {
		if (IntentUtils.hasIntentBomb(intent)) {
			LogUtil.e(TAG, "onStart : hasIntentBomb");
		}
		try {
			super.onStart(new SafeIntent(intent), startId);
		} catch (Exception e) {
			LogUtil.e(TAG, "onStart: " + e.getMessage(), true);
		}
	}

	@Override
	public void onDestroy() {
		try {
			super.onDestroy();
		} catch (Exception e) {
			LogUtil.e(TAG, "onDestroy: " + e.getMessage(), true);
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		try {
			return super.onUnbind(new SafeIntent(intent));
		} catch (Exception e) {
			LogUtil.e(TAG, "onUnbind: " + e.getMessage(), true);
		}
		return false;
	}

	@Override
	public void onRebind(Intent intent) {
		try {
			super.onRebind(new SafeIntent(intent));
		} catch (Exception e) {
			LogUtil.e(TAG, "onRebind: " + e.getMessage(), true);
		}
	}

	@Override
	public ComponentName startForegroundService(Intent service) {
		try {
			return super.startForegroundService(new SafeIntent(service));
		} catch (Exception e) {
			LogUtil.e(TAG, "startForegroundService: " + e.getMessage(), true);
		}
		return null;
	}

	@Override
	public void startActivity(Intent intent) {
		try {
			super.startActivity(new SafeIntent(intent));
		} catch (Exception e) {
			LogUtil.e(TAG, "startActivity: " + e.getMessage(), true);
		}
	}

	@Override
	public void startActivities(Intent[] intents) {
		try {
			super.startActivities(intents);
		} catch (Exception e) {
			LogUtil.e(TAG, "startActivities: " + e.getMessage(), true);
		}
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		try {
			return super.bindService(service, conn, flags);
		} catch (Exception e) {
			LogUtil.e(TAG, "bindService: " + e.getMessage(), true);
		}
		return false;

	}

	@Override
	public ComponentName startService(Intent service) {
		try {
			return super.startService(service);
		} catch (Exception e) {
			LogUtil.e(TAG, "startService: " + e.getMessage(), true);
		}
		return null;
	}

	@Override
	public boolean stopService(Intent name) {
		try {
			return super.stopService(name);
		} catch (Exception e) {
			LogUtil.e(TAG, "stopService: " + e.getMessage(), true);
		}
		return false;
	}


	@Override
	public void unbindService(ServiceConnection conn) {
		try {
			super.unbindService(conn);
		} catch (Exception e) {
			LogUtil.e(TAG, "unbindService: " + e.getMessage(), true);
		}
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		try {
			super.unregisterReceiver(receiver);
		} catch (Exception e) {
			LogUtil.e(TAG, "unregisterReceiver: " + e.getMessage(), true);
		}
	}
}
