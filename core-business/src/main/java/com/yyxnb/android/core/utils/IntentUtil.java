package com.yyxnb.android.core.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * IntentUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/13
 */
public class IntentUtil {

	private IntentUtil() {
	}

	public static Intent getLaunchAppIntent(final String pkgName) {
		String launcherActivity = UtilInner.getLauncherActivity(pkgName);
		if (UtilInner.isSpace(launcherActivity)) {
			return null;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClassName(pkgName, launcherActivity);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
