package com.yyxnb.android.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yyxnb.android.utils.LogUtil;

import java.util.Arrays;
import java.util.Map;

/**
 * SafeWebView
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class SafeWebView extends WebView {

	/**
	 * 安全Webview包含两个安全
	 * <p>
	 * 一、安全webViewSettings
	 * 1)通过以下方式移除该JavaScript接口{@link SafeWebView#removeUnSafeJavascriptImpl()}
	 * ：
	 * removeJavascriptInterface("searchBoxJavaBridge_")
	 * removeJavascriptInterface("accessibility")；
	 * removeJavascriptInterface("accessibilityTraversal")
	 * 2)通过WebSettings.setSavePassword(false)关闭密码保存提醒功能
	 * {@link SafeWebView#disablePasswordStorage()}
	 * 3)通过以下设置，防止越权访问，跨域等安全问题：{@link SafeWebView#disableFileCrossAccess()}
	 * setAllowFileAccess(false)
	 * setAllowFileAccessFromFileURLs(false)
	 * setAllowUniversalAccessFromFileURLs(false)
	 * 4)关闭地理位置开关：WebSettings.setGeolocationEnabled(false);
	 * {@link SafeWebView#disableGeolocation()}
	 * 5)关闭混合内容：WebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
	 * {@link SafeWebView#disableMixedContentMode()}
	 * 6)关闭content URL读取功能
	 * {@link SafeWebView#disableContentAccess()}
	 * ()}
	 * <p>
	 * 二、安全WebViewClient
	 * 1)提供isWhiteListUri方法，在{@link SafeWebviewClientWrapper#onPageStarted(WebView, String, Bitmap)}
	 * 方法内拦截url进行url白名单校验。校验失败时加载默认错误页面(需提前通过{@link SafeWebView#setDefaultErrorPage(String)}
	 * 方法设置错误页面)
	 * 2)https证书校验失败时，默认断开连接{@link SafeWebviewClientWrapper#onReceivedSslError(WebView, SslErrorHandler, SslError)}
	 * 3)提供一个可选的showNoticeWhenSSLErrorOccurred方法处理建立https链接出现证书校验错误场景
	 */

	private static final String TAG = SafeWebView.class.getSimpleName();


	/**
	 * 当url白名单校验失败时，要加载的默认url，建议预置到apk内，保存在assets目录下
	 */
	private String defaultErrorPage;

	/**
	 * 白名单列表
	 */
	private String[] whitelist;

	/**
	 * 不匹配子域名的白名单
	 */
	private String[] whitelistNotMatchSubDomain;

	/**
	 * 白名单列表（含path的白名单）
	 */
	private String[] whitelistWithPath;

	/**
	 * 校验失败的回调接口，只有当没有设置错误页面的时候才生效
	 */
	private WebViewLoadCallBack webViewLoadCallBack;

	/**
	 * Constructs a new WebView with a Context object.
	 *
	 * @param context a Context object used to access application assets
	 */
	public SafeWebView(Context context) {
		super(context);
		initWebviewSettings();
	}

	/**
	 * Constructs a new WebView with layout parameters.
	 *
	 * @param context a Context object used to access application assets
	 * @param attrs   an AttributeSet passed to our parent
	 */
	public SafeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWebviewSettings();
	}

	/**
	 * Constructs a new WebView with layout parameters and a default style.
	 *
	 * @param context      a Context object used to access application assets
	 * @param attrs        an AttributeSet passed to our parent
	 * @param defStyleAttr an attribute in the current theme that contains a
	 *                     reference to a style resource that supplies default values for
	 */
	public SafeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initWebviewSettings();
	}

	/**
	 * Constructs a new WebView with layout parameters and a default style.
	 *
	 * @param context      a Context object used to access application assets
	 * @param attrs        an AttributeSet passed to our parent
	 * @param defStyleAttr an attribute in the current theme that contains a
	 *                     reference to a style resource that supplies default values for
	 *                     the view. Can be 0 to not look for defaults.
	 * @param defStyleRes  a resource identifier of a style resource that
	 *                     supplies default values for the view, used only if
	 *                     defStyleAttr is 0 or can not be found in the theme. Can be 0
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SafeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initWebviewSettings();
	}

	/**
	 * 获取白名单列表
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Deprecated
	public String[] getWhitelist() {
		return (this.whitelist == null ? null : Arrays.copyOf(this.whitelist, this.whitelist.length));
	}

	/**
	 * 配置白名单列表
	 *
	 * @param whitelist
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Deprecated
	public void setWhitelist(String[] whitelist) {
		this.whitelist = (whitelist == null ? null : Arrays.copyOf(whitelist, whitelist.length));
	}

	/**
	 * 获取白名单列表
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public String[] getWhitelistWithPath() {
		return (this.whitelistWithPath == null
				? null
				: Arrays.copyOf(this.whitelistWithPath, this.whitelistWithPath.length));
	}

	/**
	 * 配置白名单列表（含path的白名单）
	 *
	 * @param whitelistWithPath
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void setWhitelistWithPath(String[] whitelistWithPath) {
		this.whitelistWithPath = (whitelistWithPath == null
				? null
				: Arrays.copyOf(whitelistWithPath, whitelistWithPath.length));
	}

	/**
	 * 返回不匹配子域名的白名单
	 * 单词拼写错了，标记成Deprecated，新增正确方法
	 *
	 * @return
	 */
	@Deprecated
	public String[] getWhitelistNotMathcSubDomain() {
		return (this.whitelistNotMatchSubDomain == null
				? null
				: Arrays.copyOf(this.whitelistNotMatchSubDomain, this.whitelistNotMatchSubDomain.length));
	}

	public String[] getWhitelistNotMatchSubDomain() {
		return (this.whitelistNotMatchSubDomain == null
				? null
				: Arrays.copyOf(this.whitelistNotMatchSubDomain, this.whitelistNotMatchSubDomain.length));
	}

	/**
	 * set 不匹配子域名的白名单
	 * 单词拼写错了，标记成Deprecated，新增正确方法
	 *
	 * @param whitelistNotMatchSubDomain
	 */
	@Deprecated
	public void setWhitelistNotMathcSubDomain(String[] whitelistNotMatchSubDomain) {
		this.whitelistNotMatchSubDomain = (whitelistNotMatchSubDomain == null
				? null
				: Arrays.copyOf(whitelistNotMatchSubDomain, whitelistNotMatchSubDomain.length));
	}

	public void setWhitelistNotMatchSubDomain(String[] whitelistNotMathcSubDomain) {
		this.whitelistNotMatchSubDomain = (whitelistNotMathcSubDomain == null
				? null
				: Arrays.copyOf(whitelistNotMathcSubDomain, whitelistNotMathcSubDomain.length));
	}

	/**
	 * 加载的url不在白名单时，就加载这个默认页面，默认url
	 *
	 * @return
	 */
	public String getDefaultErrorPage() {
		return this.defaultErrorPage;
	}

	/**
	 * 加载的url不在白名单时，就加载这个默认页面，默认url
	 *
	 * @param url
	 */
	public void setDefaultErrorPage(String url) {
		this.defaultErrorPage = url;
	}

	/**
	 * 重载loadUrl，不允许加载http协议的url
	 *
	 * @param url
	 */
	@Override
	public void loadUrl(String url) {
		if (!isWhiteListUrl(url)) {
			Log.e(TAG, "loadUrl: url is not in white list");
			return;
		}
		if (isHttpUrl(url)) {
			Log.e(TAG, "loadUrl: http url , not safe");
			if (!TextUtils.isEmpty(defaultErrorPage)) {
				super.loadUrl(defaultErrorPage);
			} else if (getWebViewLoadCallBack() != null) {
				Log.e(TAG, "WebViewLoadCallBack");
				getWebViewLoadCallBack().onCheckError(url, WebViewLoadCallBack.ErrorCode.HTTP_URL);
			}
			return;
		}
		super.loadUrl(url);
	}

	@Override
	public void postUrl(String url, byte[] postData) {
		if (!isWhiteListUrl(url)) {
			Log.e(TAG, "postUrl: url is not in white list");
			return;
		}
		if (isHttpUrl(url)) {
			Log.e(TAG, "postUrl: http url , not safe");
			if (!TextUtils.isEmpty(defaultErrorPage)) {
				super.postUrl(defaultErrorPage, postData);
			} else if (getWebViewLoadCallBack() != null) {
				Log.e(TAG, "WebViewLoadCallBack");
				getWebViewLoadCallBack().onCheckError(url, WebViewLoadCallBack.ErrorCode.HTTP_URL);
			}
			return;
		}
		super.postUrl(url, postData);
	}

	@Override
	public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
		if (!isWhiteListUrl(url)) {
			Log.e(TAG, "loadUrl: url is not in white list");
			return;
		}
		if (isHttpUrl(url)) {
			Log.e(TAG, "loadUrl: http url , not safe");
			if (!TextUtils.isEmpty(defaultErrorPage)) {
				super.loadUrl(defaultErrorPage, additionalHttpHeaders);
			} else if (getWebViewLoadCallBack() != null) {
				Log.e(TAG, "WebViewLoadCallBack");
				getWebViewLoadCallBack().onCheckError(url, WebViewLoadCallBack.ErrorCode.HTTP_URL);
			}
			return;
		}
		super.loadUrl(url, additionalHttpHeaders);
	}

	@Override
	public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
		if (isHttpUrl(baseUrl)) {
			Log.e(TAG, "loadDataWithBaseURL: http url , not safe");
			if (!TextUtils.isEmpty(defaultErrorPage)) {
				super.loadDataWithBaseURL(defaultErrorPage, data, mimeType, encoding, historyUrl);
			} else if (getWebViewLoadCallBack() != null) {
				Log.e(TAG, "WebViewLoadCallBack");
				getWebViewLoadCallBack().onCheckError(baseUrl, WebViewLoadCallBack.ErrorCode.HTTP_URL);
			}
			return;
		}
		super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
	}

	/**
	 * 是否是http协议的url
	 *
	 * @param url
	 * @return
	 */
	private boolean isHttpUrl(String url) {
		return URLUtil.isHttpUrl(url);
	}

	/**
	 * Sets the WebViewClient that will receive various notifications and
	 * requests. This will replace the current handler.
	 * 默认在onPageStarted函数内拦截url并校验url，如果校验通过，则继续加载，如果校验失败，则加载defaultUrl
	 *
	 * @param client an implementation of WebViewClient
	 */
	@Override
	public void setWebViewClient(WebViewClient client) {
		super.setWebViewClient(new SafeWebviewClientWrapper(client, true));
	}

	/**
	 * 如果业务自己实现了onPageStarted，必须调用这个函数，并将useDefaultSafeWebViewClient传入false才能生效
	 *
	 * @param client
	 * @param useDefaultSafeWebViewClient 是否自己实现了onPageStarted函数
	 */
	public void setWebViewClient(WebViewClient client, boolean useDefaultSafeWebViewClient) {
		super.setWebViewClient(new SafeWebviewClientWrapper(client, useDefaultSafeWebViewClient));
	}

	/**
	 * 初始化操作
	 */
	private void initWebviewSettings() {
		SafeWebSettings.initWebviewAndSettings(this);
		// 默认安全WebviewClient
		setWebViewClient(null);
	}

	/**
	 * 判断Uri 是否在白名单内
	 * 建议参考{@link UriUtil#isUrlHostInWhitelist(String, String[])} 和
	 * {@link UriUtil#isUrlHostMatchWhitelist(String, String)}
	 *
	 * @param url 待校验的uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public boolean isWhiteListUrl(String url) {

		if (TextUtils.isEmpty(url)) {
			LogUtil.e(TAG, "url is null");
			return false;
		}

		/**
		 * 只校验http和https协议开头的url，其它scheme不校验
		 */
		if (!URLUtil.isNetworkUrl(url)) {
			return true;
		}

		String[] whiteWithPath = this.getWhitelistWithPath();
		String[] whiteNoSubDomain = this.getWhitelistNotMatchSubDomain();
		String[] white = this.getWhitelist();
		if (whiteWithPath != null && whiteWithPath.length != 0) {
			return UriUtil.isUrlHostAndPathInWhitelist(url, whiteWithPath);
		} else if (whiteNoSubDomain != null && whiteNoSubDomain.length != 0) {
			return UriUtil.isUrlHostSameWhitelist(url, whiteNoSubDomain);
		} else {
			return UriUtil.isUrlHostInWhitelist(url, white);
		}
	}

	final public void onCheckError(WebView view, String url) {
		LogUtil.e(TAG, "onCheckError url is not in white list ", url);
		view.stopLoading();
		String defaultUrl = getDefaultErrorPage();
		if (!TextUtils.isEmpty(defaultUrl)) {
			view.loadUrl(defaultUrl);
		} else if (getWebViewLoadCallBack() != null) {
			Log.e(TAG, "onPageStarted WebViewLoadCallBack");
			getWebViewLoadCallBack().onCheckError(url, WebViewLoadCallBack.ErrorCode.URL_NOT_IN_WHITE_LIST);
		}
	}

	/**
	 * 提供一个处理方法处理加载HTTPS网页出现SSL证书校验失败场景
	 * 弹框提示用户是否继续执行加载（继续加载有风险）。
	 *
	 * @param title          弹框标题
	 * @param tips           摊款提示内容
	 * @param posiviceButton 确认按钮文案
	 * @param negaticeButton 取消按钮文案
	 * @param handler        处理继续加载的SslErrorHandler
	 */
	@Deprecated
	protected final void showNoticeWhenSSLErrorOccurred(String title, String tips, String posiviceButton,
														String negaticeButton, final SslErrorHandler handler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}
		DialogInterface.OnClickListener positiveListener = new PositiveOnClickListener(handler);
		DialogInterface.OnClickListener negativeListener = new NegativeOnClickListener(handler);
		builder.setMessage(tips);
		builder.setPositiveButton(posiviceButton, positiveListener);
		builder.setNegativeButton(negaticeButton, negativeListener);
		final AlertDialog dialog = builder.create();
		dialog.show();
	}

	public WebViewLoadCallBack getWebViewLoadCallBack() {
		return webViewLoadCallBack;
	}

	public void setWebViewLoadCallBack(WebViewLoadCallBack webViewLoadCallBack) {
		this.webViewLoadCallBack = webViewLoadCallBack;
	}

	/**
	 * findbugs修改，不能使用匿名类，必须使用静态内部类
	 */
	private static class PositiveOnClickListener implements DialogInterface.OnClickListener {
		private final SslErrorHandler handler;

		PositiveOnClickListener(SslErrorHandler handler) {
			this.handler = handler;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			handler.proceed();
		}
	}

	/**
	 * findbugs修改，不能使用匿名类，必须使用静态内部类
	 */
	private static class NegativeOnClickListener implements DialogInterface.OnClickListener {
		private final SslErrorHandler handler;

		NegativeOnClickListener(SslErrorHandler handler) {
			this.handler = handler;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			handler.cancel();
		}
	}

	/**
	 * WebviewClient装饰类
	 */
	private final class SafeWebviewClientWrapper extends WebViewClient {

		/**
		 * 调用方自定义的WebViewClient，安全组件会改变onPageStarted和onReceivedSslError两个函数的逻辑，只有设置useDefaultSafeWebViewClient为false，才会采用调用方自定义的这两个函数
		 */
		private WebViewClient mWebviewClient;

		/**
		 * 是否使用安全组件的默认url校验函数，默认值是true
		 */
		private boolean useDefaultSafeWebViewClient;

		/**
		 * 采用SafeWebviewClientWrapper装饰 WebviewClient
		 *
		 * @param webViewClient               业务自定义webviewclient
		 * @param useDefaultSafeWebViewClient 是否使用默认的url校验方法
		 */
		private SafeWebviewClientWrapper(WebViewClient webViewClient, boolean useDefaultSafeWebViewClient) {
			super();
			mWebviewClient = webViewClient;
			this.useDefaultSafeWebViewClient = useDefaultSafeWebViewClient;
		}

		/**
		 * Notify the host application of a resource request and allow the
		 * application to return the data. If the return value is null, the WebView
		 * will continue to load the resource as usual. Otherwise, the return
		 * response and data will be used. NOTE: This method is called on a thread
		 * other than the UI thread so clients should exercise caution
		 * when accessing private data or the view system.
		 *
		 * @param view    The {@link WebView} that is requesting the
		 *                resource.
		 * @param request Object containing the details of the request.
		 * @return A {@link WebResourceResponse} containing the
		 * response information or null if the WebView should load the
		 * resource itself.
		 */

		@Override
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

			if (mWebviewClient != null) {
				return mWebviewClient.shouldInterceptRequest(view, request);
			}
			return super.shouldInterceptRequest(view, request);
		}

		/**
		 * Notify the host application of a resource request and allow the
		 * application to return the data. If the return value is null, the WebView
		 * will continue to load the resource as usual. Otherwise, the return
		 * response and data will be used. NOTE: This method is called on a thread
		 * other than the UI thread so clients should exercise caution
		 * when accessing private data or the view system.
		 *
		 * @param view The {@link WebView} that is requesting the
		 *             resource.
		 * @param url  The raw url of the resource.
		 * @return A {@link WebResourceResponse} containing the
		 * response information or null if the WebView should load the
		 * resource itself.
		 * @deprecated Use {@link #shouldInterceptRequest(WebView, WebResourceRequest)
		 * shouldInterceptRequest(WebView, WebResourceRequest)} instead.
		 */

		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

			if (mWebviewClient != null) {
				return mWebviewClient.shouldInterceptRequest(view, url);
			}
			return super.shouldInterceptRequest(view, url);
		}

		/**
		 * Give the host application a chance to take over the control when a new
		 * url is about to be loaded in the current WebView. If WebViewClient is not
		 * provided, by default WebView will ask Activity Manager to choose the
		 * proper handler for the url. If WebViewClient is provided, return true
		 * means the host application handles the url, while return false means the
		 * current WebView handles the url.
		 * This method is not called for requests using the POST "method".
		 *
		 * @param view The WebView that is initiating the callback.
		 * @param url  The url to be loaded.
		 * @return True if the host application wants to leave the current WebView
		 * and handle the url itself, otherwise return false.
		 * @deprecated Use {@link #shouldOverrideUrlLoading(WebView, WebResourceRequest)
		 * shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead.
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (mWebviewClient != null) {
				return mWebviewClient.shouldOverrideUrlLoading(view, url);
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		/**
		 * Give the host application a chance to take over the control when a new
		 * url is about to be loaded in the current WebView. If WebViewClient is not
		 * provided, by default WebView will ask Activity Manager to choose the
		 * proper handler for the url. If WebViewClient is provided, return true
		 * means the host application handles the url, while return false means the
		 * current WebView handles the url.
		 * <p>
		 * <p>
		 * Notes:
		 * <ul>
		 * <li>This method is not called for requests using the POST
		 * &quot;method&quot;.</li>
		 * <li>This method is also called for subframes with non-http schemes, thus it
		 * is
		 * strongly disadvised to unconditionally call {@link WebView#loadUrl(String)}
		 * with the request's url from inside the method and then return true,
		 * as this will make WebView to attempt loading a non-http url, and thus
		 * fail.</li>
		 * </ul>
		 * </p>
		 *
		 * @param view    The WebView that is initiating the callback.
		 * @param request Object containing the details of the request.
		 * @return True if the host application wants to leave the current WebView
		 * and handle the url itself, otherwise return false.
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.N)
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			if (mWebviewClient != null) {
				return mWebviewClient.shouldOverrideUrlLoading(view, request);
			}
			return super.shouldOverrideUrlLoading(view, request);
		}

		/**
		 * Notify the host application that a page has started loading. This method
		 * is called once for each main frame load so a page with iframes or
		 * framesets will call onPageStarted one time for the main frame. This also
		 * means that onPageStarted will not be called when the contents of an
		 * embedded frame changes, i.e. clicking a link whose target is an iframe,
		 * it will also not be called for fragment navigations (navigations to
		 * #fragment_id).
		 * 在此函数内拦截要加载的url，如果业务本身未设置useDefaultSafeWebViewClient或者设置为true，则调用安全组件提供的校验方法去校验白名单。
		 * 如果业务设置为false，则调用业务的onPageStarted方法，安全组件的方法不再有效
		 *
		 * @param view    The WebView that is initiating the callback.
		 * @param url     The url to be loaded.
		 * @param favicon The favicon for this page if it already exists in the
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			if (mWebviewClient != null && !useDefaultSafeWebViewClient) {
				mWebviewClient.onPageStarted(view, url, favicon);
				return;
			}

			if (!isWhiteListUrl(url)) {
				onCheckError(view, url);
				return;
			}
			super.onPageStarted(view, url, favicon);
		}

		/**
		 * Notify the host application that a page has finished loading. This method
		 * is called only for main frame. When onPageFinished() is called, the
		 * rendering picture may not be updated yet. To get the notification for the
		 * new Picture, use {@link PictureListener#onNewPicture}.
		 *
		 * @param view The WebView that is initiating the callback.
		 * @param url  The url of the page.
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			if (mWebviewClient != null) {
				mWebviewClient.onPageFinished(view, url);
			} else {
				super.onPageFinished(view, url);
			}
		}

		/**
		 * Notify the host application that the WebView will load the resource
		 * specified by the given url.
		 *
		 * @param view The WebView that is initiating the callback.
		 * @param url  The url of the resource the WebView will load.
		 */
		@Override
		public void onLoadResource(WebView view, String url) {
			if (mWebviewClient != null) {
				mWebviewClient.onLoadResource(view, url);
			} else {
				super.onLoadResource(view, url);
			}
		}

		/**
		 * Notify the host application that {@link WebView} content left over from
		 * previous page navigations will no longer be drawn.
		 * <p>
		 * <p>
		 * This callback can be used to determine the point at which it is safe to make
		 * a recycled
		 * {@link WebView} visible, ensuring that no stale content is shown. It is
		 * called
		 * at the earliest point at which it can be guaranteed that
		 * {@link WebView#onDraw} will no
		 * longer draw any content from previous navigations. The next draw will display
		 * either the
		 * {@link WebView#setBackgroundColor background color} of the {@link WebView},
		 * or some of the
		 * contents of the newly loaded page.
		 * <p>
		 * <p>
		 * This method is called when the body of the HTTP response has started loading,
		 * is reflected
		 * in the DOM, and will be visible in subsequent draws. This callback occurs
		 * early in the
		 * document loading process, and as such you should expect that linked resources
		 * (for example,
		 * css and images) may not be available.
		 * </p>
		 * <p>
		 * <p>
		 * For more fine-grained notification of visual state updates, see {@link
		 * WebView#postVisualStateCallback}.
		 * </p>
		 * <p>
		 * <p>
		 * Please note that all the conditions and recommendations applicable to
		 * {@link WebView#postVisualStateCallback} also apply to this API.
		 * <p>
		 * <p>
		 * <p>
		 * This callback is only called for main frame navigations.
		 * </p>
		 *
		 * @param view The {@link WebView} for which the navigation occurred.
		 * @param url  The URL corresponding to the page navigation that triggered this
		 *             callback.
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.M)
		public void onPageCommitVisible(WebView view, String url) {
			if (mWebviewClient != null) {
				mWebviewClient.onPageCommitVisible(view, url);
			} else {
				super.onPageCommitVisible(view, url);
			}
		}

		/**
		 * Notify the host application that there have been an excessive number of
		 * HTTP redirects. As the host application if it would like to continue
		 * trying to load the resource. The default behavior is to send the cancel
		 * message.
		 *
		 * @param view        The WebView that is initiating the callback.
		 * @param cancelMsg   The message to send if the host wants to cancel
		 * @param continueMsg The message to send if the host wants to continue
		 * @deprecated This method is no longer called. When the WebView encounters
		 * a redirect loop, it will cancel the load.
		 */
		@Override
		public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
			if (mWebviewClient != null) {
				mWebviewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
			} else {
				super.onTooManyRedirects(view, cancelMsg, continueMsg);
			}
		}

		/**
		 * Report an error to the host application. These errors are unrecoverable
		 * (i.e. the main resource is unavailable). The errorCode parameter
		 * corresponds to one of the ERROR_* constants.
		 *
		 * @param view        The WebView that is initiating the callback.
		 * @param errorCode   The error code corresponding to an ERROR_* value.
		 * @param description A String describing the error.
		 * @param failingUrl  The url that failed to load.
		 * @deprecated Use
		 * {@link #onReceivedError(WebView, WebResourceRequest, WebResourceError)
		 * onReceivedError(WebView, WebResourceRequest, WebResourceError)}
		 * instead.
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedError(view, errorCode, description, failingUrl);
			} else {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		}

		/**
		 * Report web resource loading error to the host application. These errors
		 * usually indicate
		 * inability to connect to the server. Note that unlike the deprecated version
		 * of the callback,
		 * the new version will be called for any resource (iframe, image, etc), not
		 * just for the main
		 * page. Thus, it is recommended to perform minimum required work in this
		 * callback.
		 *
		 * @param view    The WebView that is initiating the callback.
		 * @param request The originating request.
		 * @param error   Information about the error occured.
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.M)
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedError(view, request, error);
			} else {
				super.onReceivedError(view, request, error);
			}
		}

		/**
		 * Notify the host application that an HTTP error has been received from the
		 * server while
		 * loading a resource. HTTP errors have status codes &gt;= 400. This callback
		 * will be called
		 * for any resource (iframe, image, etc), not just for the main page. Thus, it
		 * is recommended to
		 * perform minimum required work in this callback. Note that the content of the
		 * server
		 * response may not be provided within the <b>errorResponse</b> parameter.
		 *
		 * @param view          The WebView that is initiating the callback.
		 * @param request       The originating request.
		 * @param errorResponse Information about the error occured.
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.M)
		public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedHttpError(view, request, errorResponse);
			} else {
				super.onReceivedHttpError(view, request, errorResponse);
			}
		}

		/**
		 * As the host application if the browser should resend data as the
		 * requested page was a result of a POST. The default is to not resend the
		 * data.
		 *
		 * @param view       The WebView that is initiating the callback.
		 * @param dontResend The message to send if the browser should not resend
		 * @param resend     The message to send if the browser should resend data
		 */
		@Override
		public void onFormResubmission(WebView view, Message dontResend, Message resend) {
			if (mWebviewClient != null) {
				mWebviewClient.onFormResubmission(view, dontResend, resend);
			} else {
				super.onFormResubmission(view, dontResend, resend);
			}
		}

		/**
		 * Notify the host application to update its visited links database.
		 *
		 * @param view     The WebView that is initiating the callback.
		 * @param url      The url being visited.
		 * @param isReload True if this url is being reloaded.
		 */
		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
			if (mWebviewClient != null) {
				mWebviewClient.doUpdateVisitedHistory(view, url, isReload);
			} else {
				super.doUpdateVisitedHistory(view, url, isReload);
			}
		}

		/**
		 * Notify the host application to handle a SSL client certificate
		 * request. The host application is responsible for showing the UI
		 * if desired and providing the keys. There are three ways to
		 * respond: proceed(), cancel() or ignore(). Webview stores the response
		 * in memory (for the life of the application) if proceed() or cancel() is
		 * called and does not call onReceivedClientCertRequest() again for the
		 * same host and port pair. Webview does not store the response if ignore()
		 * is called. Note that, multiple layers in chromium network stack might be
		 * caching the responses, so the behavior for ignore is only a best case
		 * effort.
		 * <p>
		 * This method is called on the UI thread. During the callback, the
		 * connection is suspended.
		 * <p>
		 * For most use cases, the application program should implement the
		 * {@link KeyChainAliasCallback} interface and pass it to
		 * {@link KeyChain#choosePrivateKeyAlias} to start an
		 * activity for the user to choose the proper alias. The keychain activity will
		 * provide the alias through the callback method in the implemented interface.
		 * Next
		 * the application should create an async task to call
		 * {@link KeyChain#getPrivateKey} to receive the key.
		 * <p>
		 * An example implementation of client certificates can be seen at
		 * <A href=
		 * "https://android.googlesource.com/platform/packages/apps/Browser/+/android-5.1.1_r1/src/com/android/browser/Tab.java">
		 * AOSP Browser</a>
		 * <p>
		 * The default behavior is to cancel, returning no client certificate.
		 *
		 * @param view    The WebView that is initiating the callback
		 * @param request An instance of a {@link ClientCertRequest}
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedClientCertRequest(view, request);
			} else {
				super.onReceivedClientCertRequest(view, request);
			}
		}

		/**
		 * Notifies the host application that the WebView received an HTTP
		 * authentication request. The host application can use the supplied
		 * {@link HttpAuthHandler} to set the WebView's response to the request.
		 * The default behavior is to cancel the request.
		 *
		 * @param view    the WebView that is initiating the callback
		 * @param handler the HttpAuthHandler used to set the WebView's response
		 * @param host    the host requiring authentication
		 * @param realm   the realm for which authentication is required
		 * @see WebView#getHttpAuthUsernamePassword
		 */
		@Override
		public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
			} else {
				super.onReceivedHttpAuthRequest(view, handler, host, realm);
			}
		}

		/**
		 * Give the host application a chance to handle the key event synchronously.
		 * e.g. menu shortcut key events need to be filtered this way. If return
		 * true, WebView will not handle the key event. If return false, WebView
		 * will always handle the key event, so none of the super in the view chain
		 * will see the key event. The default behavior returns false.
		 *
		 * @param view  The WebView that is initiating the callback.
		 * @param event The key event.
		 * @return True if the host application wants to handle the key event
		 * itself, otherwise return false
		 */
		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			if (mWebviewClient != null) {
				return mWebviewClient.shouldOverrideKeyEvent(view, event);
			}
			return super.shouldOverrideKeyEvent(view, event);
		}

		/**
		 * Notify the host application that a key was not handled by the WebView.
		 * Except system keys, WebView always consumes the keys in the normal flow
		 * or if shouldOverrideKeyEvent returns true. This is called asynchronously
		 * from where the key is dispatched. It gives the host application a chance
		 * to handle the unhandled key events.
		 *
		 * @param view  The WebView that is initiating the callback.
		 * @param event The key event.
		 */
		@Override
		public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
			if (mWebviewClient != null) {
				mWebviewClient.onUnhandledKeyEvent(view, event);
			} else {
				super.onUnhandledKeyEvent(view, event);
			}
		}

		/**
		 * http://androidxref.com/8.0.0_r4/xref/frameworks/base/core/java/android/webkit
		 * /WebViewClient.java
		 * onUnhandledInputEvent接口无法override
		 * Notify host application that the given WebView's render process has exited.
		 * <p>
		 * Multiple WebView instances may be associated with a single render process;
		 * onRenderProcessGone will be called for each WebView that was affected.
		 * The application's implementation of this callback should only attempt to
		 * clean up the specific WebView given as a parameter, and should not assume
		 * that other WebView instances are affected.
		 * <p>
		 * The given WebView can't be used, and should be removed from the view
		 * all references to it should be cleaned up, e.g any references in the Activity
		 * or other classes saved using {@link android.view.View#findViewById} and
		 * similar calls, etc.
		 * <p>
		 * To cause an render process crash for test purpose, the application can
		 * call {@code loadUrl("chrome://crash")} on the WebView. Note that multiple
		 * WebView
		 * instances may be affected if they share a render process, not just the
		 * specific WebView which loaded chrome://crash.
		 *
		 * @param view   The WebView which needs to be cleaned up.
		 * @param detail the reason why it exited.
		 * @return {@code true} if the host application handled the situation that
		 * process has
		 * exited, otherwise, application will crash if render process crashed,
		 * or be killed if render process was killed by the system.
		 * 参考：https://developer.android.com/guide/webapps/managing-webview?hl=zh-CN
		 */
		@SuppressLint("NewApi")
		@Override
		public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
			if (mWebviewClient != null) {
				return mWebviewClient.onRenderProcessGone(view, detail);
			}
			return super.onRenderProcessGone(view, detail);
		}

		/**
		 * Notify the host application that a loading URL has been flagged by Safe
		 * Browsing.
		 * <p>
		 * The application must invoke the callback to indicate the preferred response.
		 * The default
		 * behavior is to show an interstitial to the user, with the reporting checkbox
		 * visible.
		 * <p>
		 * If the application needs to show its own custom interstitial UI, the callback
		 * can be invoked
		 * asynchronously with {@link SafeBrowsingResponse#backToSafety} or {@link
		 * SafeBrowsingResponse#proceed}, depending on user response.
		 *
		 * @param view       The WebView that hit the malicious resource.
		 * @param request    Object containing the details of the request.
		 * @param threatType The reason the resource was caught by Safe Browsing,
		 *                   corresponding to a
		 *                   {@code SAFE_BROWSING_THREAT_*} value.
		 * @param callback   Applications must invoke one of the callback methods.
		 *                   参考：https://developer.android.com/guide/webapps/managing-webview?hl=zh-CN
		 */
		@SuppressLint("NewApi")
		@Override
		public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType,
									  SafeBrowsingResponse callback) {
			if (mWebviewClient != null) {
				mWebviewClient.onSafeBrowsingHit(view, request, threatType, callback);
			} else {
				super.onSafeBrowsingHit(view, request, threatType, callback);
			}
		}

		/**
		 * Notify the host application that the scale applied to the WebView has
		 * changed.
		 *
		 * @param view     The WebView that is initiating the callback.
		 * @param oldScale The old scale factor
		 * @param newScale The new scale factor
		 */
		@Override
		public void onScaleChanged(WebView view, float oldScale, float newScale) {
			if (mWebviewClient != null) {
				mWebviewClient.onScaleChanged(view, oldScale, newScale);
			} else {
				super.onScaleChanged(view, oldScale, newScale);
			}
		}

		/**
		 * Notify the host application that a request to automatically log in the
		 * user has been processed.
		 *
		 * @param view    The WebView requesting the login.
		 * @param realm   The account realm used to look up accounts.
		 * @param account An optional account. If not null, the account should be
		 *                checked against accounts on the device. If it is a valid
		 *                account, it should be used to log in the user.
		 * @param args    Authenticator specific arguments used to log in the user.
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
		public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedLoginRequest(view, realm, account, args);
			} else {
				super.onReceivedLoginRequest(view, realm, account, args);
			}
		}

		/**
		 * 证书校验失败时默认关闭连接，只有当useDefaultSafeWebViewClient为false时，才采用集成方的证书校验错误处理逻辑
		 *
		 * @param view    The WebView that is initiating the callback.
		 * @param handler An SslErrorHandler object that will handle the user's
		 *                response.
		 * @param error   The SSL error object.
		 */
		@Override
		@TargetApi(Build.VERSION_CODES.FROYO)
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			if (mWebviewClient != null) {
				mWebviewClient.onReceivedSslError(view, handler, error);
			} else {
				super.onReceivedSslError(view, handler, error);
			}
		}
	}
}