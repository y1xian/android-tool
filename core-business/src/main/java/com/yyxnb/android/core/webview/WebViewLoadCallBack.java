package com.yyxnb.android.core.webview;

/**
 * WebViewLoadCallBack
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public interface WebViewLoadCallBack {

	void onCheckError(String url, ErrorCode errorCode);

	enum ErrorCode {
		/**
		 * HTTP的url，建议使用系统浏览器加载
		 */
		HTTP_URL,
		/**
		 * 不在白名单的url
		 */
		URL_NOT_IN_WHITE_LIST,
		/**
		 * 其它类型
		 */
		OTHER
	}
}
