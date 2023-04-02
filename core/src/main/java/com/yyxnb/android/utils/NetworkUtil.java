package com.yyxnb.android.utils;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

import com.yyxnb.android.enums.NetworkType;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * NetworkUtil
 *
 * <pre>
 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class NetworkUtil {

	private NetworkUtil() {
	}

	/**
	 * Ipv4 address check.
	 */
	private static final Pattern IPV4_PATTERN = Pattern.compile(
			"^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
					"([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

	/**
	 * Check if valid IPV4 address.
	 *
	 * @param input the address string to check for validity.
	 * @return True if the input parameter is a valid IPv4 address.
	 */
	public static boolean isIPv4Address(String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}


	public static void openWirelessSettings() {
		UtilInner.getApp().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		);
	}

	@RequiresPermission(ACCESS_NETWORK_STATE)
	public static boolean isConnected() {
		NetworkInfo info = getActiveNetworkInfo();
		return info != null && info.isConnected();
	}

	@RequiresPermission(ACCESS_NETWORK_STATE)
	private static NetworkInfo getActiveNetworkInfo() {
		ConnectivityManager cm =
				(ConnectivityManager) UtilInner.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return null;
		}
		return cm.getActiveNetworkInfo();
	}

	@RequiresPermission(ACCESS_NETWORK_STATE)
	private static boolean isEthernet() {
		final ConnectivityManager cm =
				(ConnectivityManager) UtilInner.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		if (info == null) {
			return false;
		}
		NetworkInfo.State state = info.getState();
		if (null == state) {
			return false;
		}
		return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
	}

	public static boolean getMobileDataEnabled() {
		try {
			TelephonyManager tm =
					(TelephonyManager) UtilInner.getApp().getSystemService(Context.TELEPHONY_SERVICE);
			if (tm == null) {
				return false;
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				return tm.isDataEnabled();
			}
			@SuppressLint("PrivateApi")
			Method getMobileDataEnabledMethod =
					tm.getClass().getDeclaredMethod("getDataEnabled");
			if (null != getMobileDataEnabledMethod) {
				return (boolean) getMobileDataEnabledMethod.invoke(tm);
			}
		} catch (Exception e) {
			UtilInner.e(e);
		}
		return false;
	}

	@RequiresPermission(ACCESS_NETWORK_STATE)
	public static boolean isMobileData() {
		NetworkInfo info = getActiveNetworkInfo();
		return null != info
				&& info.isAvailable()
				&& info.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	@RequiresPermission(ACCESS_NETWORK_STATE)
	public static boolean is4G() {
		NetworkInfo info = getActiveNetworkInfo();
		return info != null
				&& info.isAvailable()
				&& info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
	}

	@RequiresPermission(ACCESS_NETWORK_STATE)
	public static boolean is5G() {
		NetworkInfo info = getActiveNetworkInfo();
		return info != null
				&& info.isAvailable()
				&& info.getSubtype() == TelephonyManager.NETWORK_TYPE_NR;
	}

	@RequiresPermission(ACCESS_WIFI_STATE)
	public static boolean getWifiEnabled() {
		@SuppressLint("WifiManagerLeak")
		WifiManager manager = (WifiManager) UtilInner.getApp().getSystemService(WIFI_SERVICE);
		if (manager == null) {
			return false;
		}
		return manager.isWifiEnabled();
	}

	@RequiresPermission(CHANGE_WIFI_STATE)
	public static void setWifiEnabled(final boolean enabled) {
		@SuppressLint("WifiManagerLeak")
		WifiManager manager = (WifiManager) UtilInner.getApp().getSystemService(WIFI_SERVICE);
		if (manager == null) {
			return;
		}
		if (enabled == manager.isWifiEnabled()) {
			return;
		}
		manager.setWifiEnabled(enabled);
	}


	@RequiresPermission(ACCESS_NETWORK_STATE)
	public static NetworkType getNetworkType() {
		if (isEthernet()) {
			return NetworkType.NETWORK_ETHERNET;
		}
		NetworkInfo info = getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				return NetworkType.NETWORK_WIFI;
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (info.getSubtype()) {
					case TelephonyManager.NETWORK_TYPE_GSM:
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						return NetworkType.NETWORK_2G;

					case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						return NetworkType.NETWORK_3G;

					case TelephonyManager.NETWORK_TYPE_IWLAN:
					case TelephonyManager.NETWORK_TYPE_LTE:
						return NetworkType.NETWORK_4G;

					case TelephonyManager.NETWORK_TYPE_NR:
						return NetworkType.NETWORK_5G;
					default:
						String subtypeName = info.getSubtypeName();
						if (subtypeName.equalsIgnoreCase("TD-SCDMA")
								|| subtypeName.equalsIgnoreCase("WCDMA")
								|| subtypeName.equalsIgnoreCase("CDMA2000")) {
							return NetworkType.NETWORK_3G;
						} else {
							return NetworkType.NETWORK_UNKNOWN;
						}
				}
			} else {
				return NetworkType.NETWORK_UNKNOWN;
			}
		}
		return NetworkType.NETWORK_NO;
	}

	/**
	 * Get local Ip address.
	 *
	 * @return InetAddress
	 */
	public static InetAddress getLocalIpAddress() {
		Enumeration<NetworkInterface> enumeration = null;
		try {
			enumeration = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				NetworkInterface nif = enumeration.nextElement();
				Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
				if (inetAddresses != null) {
					while (inetAddresses.hasMoreElements()) {
						InetAddress inetAddress = inetAddresses.nextElement();
						if (!inetAddress.isLoopbackAddress() && isIPv4Address(inetAddress.getHostAddress())) {
							return inetAddress;
						}
					}
				}
			}
		}
		return null;
	}

	public static void registerNetworkStatusChangedListener(final OnNetworkStatusChangedListener listener) {
		NetworkChangedReceiver.getInstance().registerListener(listener);
	}

	public static boolean isRegisteredNetworkStatusChangedListener(final OnNetworkStatusChangedListener listener) {
		return NetworkChangedReceiver.getInstance().isRegistered(listener);
	}

	public static void unregisterNetworkStatusChangedListener(final OnNetworkStatusChangedListener listener) {
		NetworkChangedReceiver.getInstance().unregisterListener(listener);
	}

	public static final class NetworkChangedReceiver extends BroadcastReceiver {

		private static NetworkChangedReceiver getInstance() {
			return LazyHolder.INSTANCE;
		}

		private NetworkType mType;
		private Set<OnNetworkStatusChangedListener> mListeners = new HashSet<>();

		void registerListener(final OnNetworkStatusChangedListener listener) {
			if (listener == null) {
				return;
			}
			UtilInner.runOnUiThread(new Runnable() {
				@SuppressLint("MissingPermission")
				@Override
				public void run() {
					int preSize = mListeners.size();
					mListeners.add(listener);
					if (preSize == 0 && mListeners.size() == 1) {
						mType = getNetworkType();
						IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
						UtilInner.getApp().registerReceiver(NetworkChangedReceiver.getInstance(), intentFilter);
					}
				}
			});
		}

		boolean isRegistered(final OnNetworkStatusChangedListener listener) {
			if (listener == null) {
				return false;
			}
			return mListeners.contains(listener);
		}

		void unregisterListener(final OnNetworkStatusChangedListener listener) {
			if (listener == null) {
				return;
			}
			UtilInner.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					int preSize = mListeners.size();
					mListeners.remove(listener);
					if (preSize == 1 && mListeners.size() == 0) {
						UtilInner.getApp().unregisterReceiver(NetworkChangedReceiver.getInstance());
					}
				}
			});
		}

		@SuppressLint("MissingPermission")
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				// debouncing
				UtilInner.runOnUiThreadDelayed(() -> {
					NetworkType networkType = getNetworkType();
					if (mType == networkType) {
						return;
					}
					mType = networkType;
					if (networkType == NetworkType.NETWORK_NO) {
						for (OnNetworkStatusChangedListener listener : mListeners) {
							listener.onDisconnected();
						}
					} else {
						for (OnNetworkStatusChangedListener listener : mListeners) {
							listener.onConnected(networkType);
						}
					}
				}, 1000);
			}
		}

		private static class LazyHolder {
			private static final NetworkChangedReceiver INSTANCE = new NetworkChangedReceiver();
		}
	}

	public interface OnNetworkStatusChangedListener {
		void onDisconnected();

		void onConnected(NetworkType networkType);
	}
}
