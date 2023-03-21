package com.yyxnb.android.modules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

/**
 * IPermission
 *
 * <pre>
 *     Android 13 废弃 READ_EXTERNAL_STORAGE、WRITE_EXTERNAL_STORAGE
 *
 *     权限组列表：
 *  	Android6.0只用申请权限组中一个权限及获得全部权限
 *  	Android8.0需要全部申请权限组权限，但是只会申请第一个权限时提示，后面不会提示
 *  	// 读写日历。
 *  	Manifest.permission.READ_CALENDAR,
 *  	Manifest.permission.WRITE_CALENDAR
 *  	// 相机。
 *  	Manifest.permission.CAMERA
 *  	// 读写联系人。
 *  	Manifest.permission.READ_CONTACTS,
 *  	Manifest.permission.WRITE_CONTACTS,
 *  	Manifest.permission.GET_ACCOUNTS
 *  	// 读位置信息。
 *  	Manifest.permission.ACCESS_FINE_LOCATION,
 *  	Manifest.permission.ACCESS_COARSE_LOCATION
 *  	// 使用麦克风。
 *  	Manifest.permission.RECORD_AUDIO
 *  	// 读电话状态、打电话、读写电话记录。
 *  	Manifest.permission.READ_PHONE_STATE,
 *  	Manifest.permission.CALL_PHONE,
 *  	Manifest.permission.READ_CALL_LOG,
 *  	Manifest.permission.WRITE_CALL_LOG,
 *  	Manifest.permission.ADD_VOICEMAIL,
 *  	Manifest.permission.USE_SIP,
 *  	Manifest.permission.PROCESS_OUTGOING_CALLS
 *  	// 传感器。
 *  	Manifest.permission.BODY_SENSORS
 *  	// 读写短信、收发短信。
 *  	Manifest.permission.SEND_SMS,
 *  	Manifest.permission.RECEIVE_SMS,
 *  	Manifest.permission.READ_SMS,
 *  	Manifest.permission.RECEIVE_WAP_PUSH,
 *  	Manifest.permission.RECEIVE_MMS,
 *  	Manifest.permission.READ_CELL_BROADCASTS
 *  	// 读写存储卡。
 *  	Manifest.permission.READ_EXTERNAL_STORAGE,
 *  	Manifest.permission.WRITE_EXTERNAL_STORAGE
 * </pre>
 *
 * @author yyx
 * @date 2023/3/21
 */
public interface IPermission extends IModule {

	/**
	 * 请求权限
	 *
	 * @param context     上下文
	 * @param requestCode 请求码
	 * @param listener    监听
	 * @param permissions 需要申请的权限组
	 */
	void requestPermissions(@NonNull Context context, int requestCode
			, OnPermissionListener listener, @Size(min = 1) String... permissions);

	/**
	 * 已授权权限
	 *
	 * @param context     上下文
	 * @param permissions 权限组
	 * @return 是否已授权
	 */
	boolean hasGrantedPermissions(@NonNull Context context,
								  @Size(min = 1) String... permissions);

	/**
	 * 权限请求回调
	 */
	interface OnPermissionListener {

		/**
		 * 授权的权限
		 *
		 * @param requestCode 请求码
		 */
		void onPermissionGranted(int requestCode);

		/**
		 * 拒绝的权限
		 *
		 * @param requestCode 请求码
		 */
		void onPermissionDenied(int requestCode);
	}

}
