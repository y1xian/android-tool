package com.yyxnb.android.webview;

import android.util.Log;
import android.webkit.WebView;

import com.yyxnb.android.utils.ThreadUtil;

import java.util.concurrent.CountDownLatch;


/**
 * 功能描述 获取WebView加载的url地址，用于调用当前js接口时进行url白名单校验
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class SafeGetUrl {
	private static final String TAG = SafeGetUrl.class.getSimpleName();

	private static final long TIME_OUT = 200L;
	private String url;
	private WebView webView;


	public SafeGetUrl() {
	}

	public SafeGetUrl(WebView webView) {
		this.webView = webView;
	}

	/**
	 * 方案三 通过Handler去post一个runnable
	 *
	 * @return url
	 */
	public String getUrlMethod() {
		if (webView == null) {
			return "";
		}
		if (ThreadUtil.isInUiThread()) {
			return webView.getUrl();
		}

		final CountDownLatch downLatch = new CountDownLatch(1);
		ThreadUtil.runOnUiThread(() -> {
			setUrl(webView.getUrl());
			downLatch.countDown();
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			Log.e(TAG, "getUrlMethod: InterruptedException " + e.getMessage(), e);
		}
		return url;
	}

	public void setWebView(WebView webView) {
		this.webView = webView;
	}

	public WebView getWebView() {
		return webView;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
