package com.yyxnb.lib.andorid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.yyxnb.lib.andorid.app.AppUtil;

/**
 * 手机相关
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/8
 */
public class PhoneUtil {

	private static final String TAG = PhoneUtil.class.getSimpleName();
	private static TelephonyManager telephonyManager;
	//移动运营商编号
	private static String sNetworkOperator;

	public PhoneUtil() {
		telephonyManager = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
	}

	//获取sim卡iccid
	@SuppressLint("MissingPermission")
	public static String getIccid() {
		String iccid = "N/A";
		iccid = telephonyManager.getSimSerialNumber();
		return iccid;
	}

	//获取电话号码
	@SuppressLint({"MissingPermission", "HardwareIds"})
	@Deprecated
	public static String getNativePhoneNumber() {
		TelephonyManager tm = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		String nativePhoneNumber = "N/A";
		nativePhoneNumber = tm.getLine1Number();
		return nativePhoneNumber;
	}

	//获取手机服务商信息
	public static String getProvidersName() {
		String providersName = "N/A";
		sNetworkOperator = telephonyManager.getNetworkOperator();
		//IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		//Flog.d(TAG,"NetworkOperator="   NetworkOperator);
		if (sNetworkOperator.equals("46000") || sNetworkOperator.equals("46002")) {
			providersName = "中国移动";//中国移动
		} else if (sNetworkOperator.equals("46001")) {
			providersName = "中国联通";//中国联通
		} else if (sNetworkOperator.equals("46003")) {
			providersName = "中国电信";//中国电信
		}
		return providersName;

	}

	@SuppressLint({"MissingPermission", "HardwareIds"})
	public static String getPhoneInfo() {
		TelephonyManager tm = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		StringBuffer sb = new StringBuffer();

		sb.append(" \nLine1Number = " + tm.getLine1Number());
		sb.append(" \nNetworkOperator = " + tm.getNetworkOperator());//移动运营商编号
		sb.append(" \nNetworkOperatorName = " + tm.getNetworkOperatorName());//移动运营商名称
		sb.append(" \nSimCountryIso = " + tm.getSimCountryIso());
		sb.append(" \nSimOperator = " + tm.getSimOperator());
		sb.append(" \nSimOperatorName = " + tm.getSimOperatorName());
		sb.append(" \nSimSerialNumber = " + tm.getSimSerialNumber());
		sb.append(" \nSubscriberId(IMSI) = " + tm.getSubscriberId());
		return sb.toString();
	}

}
