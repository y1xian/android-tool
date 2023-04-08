package com.yyxnb.android.core.webview;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.yyxnb.android.utils.LogUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * UriUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class UriUtil {
	private static final String TAG = UriUtil.class.getSimpleName();

	/**
	 * 判断url的host是否在白名单内
	 *
	 * @param url                待校验的url
	 * @param whiteListUrlOrHost 白名单列表，支持url或者host
	 * @return
	 */
	public static boolean isUrlHostInWhitelist(String url, String[] whiteListUrlOrHost) {

		if (whiteListUrlOrHost == null || whiteListUrlOrHost.length == 0) {
			LogUtil.e(TAG, "whitelist is null");
			return false;
		}

		for (String item : whiteListUrlOrHost) {
			if (isUrlHostMatchWhitelist(url, item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * url的host和白名单host完全匹配，不匹配子域名
	 *
	 * @param url                待校验url
	 * @param whiteListUrlOrHost 白名单url或者host
	 * @return
	 */
	public static boolean isUrlHostSameWhitelist(String url, String whiteListUrlOrHost) {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(whiteListUrlOrHost)) {
			Log.e(TAG, "isUrlHostSameWhitelist: url or host is null");
			return false;
		}
		String urlHost = getHostByURI(url);
		String whiteHost = getWhiteListHost(whiteListUrlOrHost);
		return TextUtils.equals(urlHost, whiteHost);
	}

	/**
	 * url的host和白名单host完全匹配，不匹配子域名
	 *
	 * @param url                待校验url
	 * @param whiteListUrlOrHost 白名单url或者host
	 * @return
	 */
	public static boolean isUrlHostSameWhitelist(String url, String[] whiteListUrlOrHost) {
		if (whiteListUrlOrHost == null || whiteListUrlOrHost.length == 0) {
			LogUtil.e(TAG, "whitelist is null");
			return false;
		}

		for (String item : whiteListUrlOrHost) {
			if (isUrlHostSameWhitelist(url, item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断url的host是否匹配白名单
	 *
	 * @param url                待校验的url
	 * @param whiteListUrlOrHost 白名单url或者host，如 huawei.com，支持http://huawei.com
	 *                           https://huawei.com
	 * @return
	 */
	public static boolean isUrlHostMatchWhitelist(String url, String whiteListUrlOrHost) {

		// 获取host
		String urlHost = getHostByURI(url);
		if (TextUtils.isEmpty(urlHost) || TextUtils.isEmpty(whiteListUrlOrHost)) {
			LogUtil.e(TAG, "url or whitelist is null");
			return false;
		}

		String safeHost = getWhiteListHost(whiteListUrlOrHost);
		if (TextUtils.isEmpty(safeHost)) {
			Log.e(TAG, "whitelist host is null");
			return false;
		}

		// 1、host same with white list , return true
		if (safeHost.equals(urlHost)) {
			return true;
		}
		if (urlHost.endsWith(safeHost)) {
			try {
				// 2、Intercepts the string before the domain name，as hicloud.huawei.com , get
				// hicloud
				String hostPrefix = urlHost.substring(0, urlHost.length() - safeHost.length());

				// 3、Filter wrong host names, such as evilhuawei.com
				if (!hostPrefix.endsWith(".")) {
					return false;
				}

				// 校验是否不包含特殊字符，如果不包含，则认为校验通过，如hicloud.huawei.com，则校验通过（不过目前还未发现host包含特殊字符，因此此步目前看意义不大）
				String regularExpression = "^[A-Za-z0-9.-]+$";
				return hostPrefix.matches(regularExpression);

			} catch (IndexOutOfBoundsException e) {
				LogUtil.e(TAG, "IndexOutOfBoundsException" + e.getMessage());
				return false;
			} catch (Exception e) {
				LogUtil.e(TAG, "Exception : " + e.getMessage());
				return false;
			}
		}
		return false;
	}

	/**
	 * 使用java的URI获取host，只支持http:// 和https:// 开头的url，不支持其它协议头的url
	 * 不能使用URL获取host，原因：https://www.freebuf.com/articles/web/196165.html
	 *
	 * @param url
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String getHostByURI(final String url) {
		String tempUrl = url;
		if (TextUtils.isEmpty(tempUrl)) {
			LogUtil.i(TAG, "url is null");
			return url;
		}

		try {
			// 增加协议的校验
			if (!URLUtil.isNetworkUrl(tempUrl)) {
				LogUtil.e(TAG, "url don't starts with http or https");
				return "";
			}
			// 增加
			// https://www.booking.com/hotel/hk/renaissance-harbour-view-hong-kong.html?aid=1738669&label={%22from%22:1,%22openid%22:%22%22}
			// 参考：https://www.freebuf.com/articles/web/196165.html
			// 如果包含特殊字符 { 或者 }，则走URL的获取host方法，如果不包含，则走原来的URI的获取host方法
			// URL支持的范围广，特殊字符多，都改成URL获取，抛弃URI类
			// https://www.vmall.com\\@www.baidu.com
			// ，此URL获取host在低版本Android系统上有问题（www.baidu.com），但是在高版本系统上无问题（www.vmall.com），将 \
			// 替换成 / 就无问题了
			tempUrl = tempUrl.replaceAll("[\\\\#]", "/");
			return new URL(tempUrl).getHost();
		} catch (MalformedURLException e) {
			LogUtil.e(TAG, "getHostByURI error  MalformedURLException : " + e.getMessage());
		}
		return "";
	}

	/**
	 * 获取白名单的host，即如果白名单配置的是https://xxx.huawei.com，则取host，如果白名单就是host，则直接返回
	 * huawei.com vmall.com
	 * http://huawei.com https://huawei.com/1/2/3
	 *
	 * @param whiteListUrl
	 * @return
	 */
	private static String getWhiteListHost(final String whiteListUrl) {
		if (TextUtils.isEmpty(whiteListUrl)) {
			LogUtil.i(TAG, "whiteListUrl is null");
			return null;
		}
		if (!URLUtil.isNetworkUrl(whiteListUrl)) {
			return whiteListUrl;
		} else {
			return getHostByURI(whiteListUrl);
		}
	}

	/**
	 * 校验待加载的url是否在白名单内
	 *
	 * @param url          待校验url
	 * @param whiteListUrl 白名单url列表
	 * @return
	 */
	public static boolean isUrlHostAndPathInWhitelist(String url, String[] whiteListUrl) {
		if (whiteListUrl == null || whiteListUrl.length == 0) {
			LogUtil.e(TAG, "whitelist is null");
			return false;
		}

		for (String item : whiteListUrl) {
			if (isUrlHostAndPathMatchWhitelist(url, item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验待加载的url是否在白名单内
	 * 白名单仅支持两种形式：
	 * 1、与待加载的url完全一致或者是去掉url的参数形式的
	 * 2、待加载的url的上层path，且以 /
	 * 结尾，如待加载url：https://himovie.hicloud.com:8080/1/2/3/4/5/6.html，则可以配置白名单为：https://himovie.hicloud.com:8080/1/2/3/4/5/
	 *
	 * @param url          待校验url
	 * @param whiteListUrl 白名单url
	 * @return 校验是否通过
	 */
	public static boolean isUrlHostAndPathMatchWhitelist(String url, String whiteListUrl) {

		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(whiteListUrl)) {
			return false;
		}

		// 包含..字符
		if (url.contains("..") || url.contains("@")) {
			Log.e(TAG, "url contains unsafe char");
			return false;
		}

		// The whitelist matches the URL exactly or the URL is in the form of whitelist
		// plus parameters,
		// such as URL: https://hicloud.com?name=cc The white list is as follows:
		// https://hicloud.com
		if (whiteListUrl.equals(url) || url.startsWith(whiteListUrl + "?") || url.startsWith(whiteListUrl + "#")) {
			return true;
		}

		if (!whiteListUrl.endsWith("/")) {
			return false;
		}

		Uri whiteUri = Uri.parse(whiteListUrl);
		List<String> whiteAllPath = whiteUri.getPathSegments();

		Uri inputUri = Uri.parse(url);
		List<String> inputAllPath = inputUri.getPathSegments(); // getPathSegments，such as
		// http://huawei.com/a/b/c/，paths are a,b,c

		// The whitelist must match the deepest path of the URL. For example, the H5
		// page is himovie.hicloud.com/1/2/3/4/5/6.html,
		// then need to configure the URL to himovie.hicloud.com/1/2/3/4/5/
		if (inputAllPath.size() - whiteAllPath.size() != 1) {
			return false;
		}

		return url.startsWith(whiteListUrl);
	}
}
