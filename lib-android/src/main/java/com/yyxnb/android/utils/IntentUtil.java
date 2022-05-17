package com.yyxnb.android.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 意图操作
 *
 * <pre>
 *
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class IntentUtil {

	private static final String TAG = "IntentUtils";

	/**
	 * 获取关机的意图
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.SHUTDOWN"/>}</p>
	 *
	 * @return intent
	 */
	public static Intent getShutdownIntent() {
		Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	/**
	 * 跳至拨号界面
	 *
	 * @param context     上下文
	 * @param phoneNumber 电话号码
	 */
	public static void dial(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 拨打电话
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
	 *
	 * @param context     上下文
	 * @param phoneNumber 电话号码
	 */
	public static void call(Context context, String phoneNumber) {
		Intent intent = new Intent();
		// 启动电话程序
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel://" + phoneNumber));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 获取跳至发送短信界面的意图
	 *
	 * @param context     上下文
	 * @param phoneNumber 接收号码
	 * @param content     短信内容
	 */
	public static void sendSms(Context context, String phoneNumber, String content) {
		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 打开浏览器
	 *
	 * @param context 上下文
	 * @param url     路径
	 */
	public static void openBrowser(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}

	/**
	 * 安装APK
	 * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}
	 *
	 * @param context  上下文
	 * @param filePath 路径
	 */
	public static void installApk(Context context, String filePath) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			File file = new File(filePath);
			// 7.0+以上版本
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
			} else {
				intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			}
			context.startActivity(intent);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "安装APK异常", e);
		}
	}

	/**
	 * 卸载
	 * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}
	 *
	 * @param context 上下文
	 * @param apkFile 路径
	 */
	public static void unInstallApp(Context context, File apkFile) {
		Uri packageURI = Uri.parse("package:com.andorid.main");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

	/**
	 * 获取拍照的意图
	 *
	 * @param outUri 输出的uri
	 * @return 拍照的意图
	 */
	public static Intent getCaptureIntent(Uri outUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	/**
	 * 获取分享文本的意图
	 *
	 * @param content 分享文本
	 * @return intent
	 */
	public static Intent getShareTextIntent(String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	/**
	 * 获取分享图片的意图
	 *
	 * @param content 分享文本
	 * @param uri     图片uri
	 * @return intent
	 */
	public static Intent getShareImageIntent(String content, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setType("image/*");
		return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	/**
	 * 获取其他应用组件的意图
	 *
	 * @param packageName 包名
	 * @param className   全类名
	 * @return intent
	 */
	public static Intent getComponentIntent(String packageName, String className) {
		return getComponentIntent(packageName, className, null);
	}

	/**
	 * 获取其他应用组件的意图
	 *
	 * @param packageName 包名
	 * @param className   全类名
	 * @param bundle      bundle
	 * @return intent
	 */
	public static Intent getComponentIntent(String packageName, String className, Bundle bundle) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		ComponentName cn = new ComponentName(packageName, className);
		intent.setComponent(cn);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

}
