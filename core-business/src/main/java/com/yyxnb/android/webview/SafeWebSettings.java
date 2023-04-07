package com.yyxnb.android.webview;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * SafeWebSettings
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class SafeWebSettings {

	/**
	 * 初始化操作
	 */
	public static void initWebviewAndSettings(WebView webView) {
		WebSettings webSettings = webView.getSettings();
		// 默认安全配置
		disableFileCrossAccess(webSettings);
		removeUnSafeJavascriptImpl(webView);
		disablePasswordStorage(webSettings);
		disableGeolocation(webSettings);
		disableMixedContentMode(webSettings);
		disableContentAccess(webSettings);

	}

	/**
	 * 关闭了file协议的跨域访问，防止webview加载外部file文件读取应用内的私有文件，造成信息泄露
	 * 注意：此方法调用后，无法通过file://加载本地文件（assets目录下仍可以加载），如需加载，需重新设置为true
	 */
	public static void disableFileCrossAccess(WebSettings webSettings) {
		webSettings.setAllowFileAccess(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			webSettings.setAllowFileAccessFromFileURLs(false);
			webSettings.setAllowUniversalAccessFromFileURLs(false);
		}
	}

	/**
	 * 移除不安全的JS 接口
	 */
	public static void removeUnSafeJavascriptImpl(WebView webView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.removeJavascriptInterface("searchBoxJavaBridge_");
			webView.removeJavascriptInterface("accessibility");
			webView.removeJavascriptInterface("accessibilityTraversal");
		}
	}

	/**
	 * 低于Android API 18 需显式设置去除密码保存
	 */
	public static void disablePasswordStorage(WebSettings webSettings) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			// API 小于18 需显式设置setSavePassword为false
			webSettings.setSavePassword(false);
		}
	}

	/**
	 * 关闭定位功能，如需开启，需要重新设置为true。
	 */
	public static void disableGeolocation(WebSettings webSettings) {
		webSettings.setGeolocationEnabled(false);
	}

	/**
	 * 关闭混合内容，如需开启，需要重新设置为MIXED_CONTENT_ALWAYS_ALLOW或者MIXED_CONTENT_COMPATIBILITY_MODE。
	 */
	public static void disableMixedContentMode(WebSettings webSettings) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
		}
	}

	/**
	 * 关闭content URL（默认是开启的，存在风险）
	 */
	public static void disableContentAccess(WebSettings webSettings) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webSettings.setAllowContentAccess(false);
		}
	}

}
