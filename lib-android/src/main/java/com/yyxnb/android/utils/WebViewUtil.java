package com.yyxnb.android.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yyxnb.android.BuildConfig;
import com.yyxnb.java.StrUtil;
import com.yyxnb.java.io.FileUtil;

/**
 * WebViewUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/9
 */
public class WebViewUtil {

	public static WebSettings initSettings(Context context, WebView webView, String url) {
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setSavePassword(false);
		settings.setSaveFormData(false);
		settings.setAppCacheEnabled(false);
		settings.setDatabasePath(FileUtil.getCanonicalPath(context.getDir("", Context.MODE_PRIVATE)));
		if (StrUtil.isNotBlank(url)) {
			// 如果文件路径开头，则允许文件访问权限
			if (URLUtil.isFileUrl(url)) {
				settings.setJavaScriptEnabled(true);
				settings.setAllowContentAccess(true);
				settings.setAllowFileAccess(true);
				settings.setAllowFileAccessFromFileURLs(true);
				settings.setAllowUniversalAccessFromFileURLs(true);
				settings.setDatabaseEnabled(true);
				settings.setDomStorageEnabled(true);
				return settings;
			}
			// 如果是https://开头，允许javascript
			else if (URLUtil.isHttpsUrl(url)) {
				settings.setJavaScriptEnabled(true);
			}
			// 其余就不允许javascript
			else {
				settings.setJavaScriptEnabled(false);
			}
		} else {
			settings.setJavaScriptEnabled(false);
		}
		settings.setDatabaseEnabled(false);
		settings.setDomStorageEnabled(false);
		settings.setAllowContentAccess(false);
		settings.setAllowFileAccess(false);
		settings.setAllowFileAccessFromFileURLs(false);
		settings.setAllowUniversalAccessFromFileURLs(false);
		CookieManager.getInstance().setAcceptCookie(true);
		return settings;
	}

	public static void setLoginCookies(Context context, WebView webView, String url, String value) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager;
		(cookieManager = CookieManager.getInstance()).setAcceptCookie(true);
		if (Build.VERSION.SDK_INT >= 21) {
			cookieManager.setAcceptThirdPartyCookies(webView, true);
		}
		cookieManager.setCookie(url, value);
		CookieSyncManager.getInstance().sync();
	}
}
