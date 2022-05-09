package com.yyxnb.lib.java;

import com.yyxnb.lib.java.exceptions.UtilException;
import com.yyxnb.lib.java.http.RFC3986;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;

/**
 * 统一资源定位符相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/28
 */
public class URLUtil {

	/**
	 * 将{@link URI}转换为{@link URL}
	 *
	 * @param uri {@link URI}
	 * @return URL对象
	 * @throws UtilException {@link MalformedURLException}包装，URI格式有问题时抛出
	 * @see URI#toURL()
	 */
	public static URL url(URI uri) throws UtilException {
		if (null == uri) {
			return null;
		}
		try {
			return uri.toURL();
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 *
	 * @param urlStr URL字符串
	 * @return URL
	 */
	public static URL toUrlForHttp(String urlStr) {
		return toUrlForHttp(urlStr, null);
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 *
	 * @param urlStr  URL字符串
	 * @param handler {@link URLStreamHandler}
	 * @return URL
	 */
	public static URL toUrlForHttp(String urlStr, URLStreamHandler handler) {
		Assert.notBlank(urlStr, "Url is blank !");
		// 编码空白符，防止空格引起的请求异常
		urlStr = encodeBlank(urlStr);
		try {
			return new URL(null, urlStr, handler);
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 单独编码URL中的空白符，空白符编码为%20
	 *
	 * @param urlStr URL字符串
	 * @return 编码后的字符串
	 */
	public static String encodeBlank(CharSequence urlStr) {
		if (urlStr == null) {
			return null;
		}

		int len = urlStr.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = urlStr.charAt(i);
			if (CharUtil.isBlankChar(c)) {
				sb.append("%20");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 转URL为URI
	 *
	 * @param url URL
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 */
	public static URI toURI(URL url) throws UtilException {
		return toURI(url, false);
	}

	/**
	 * 转URL为URI
	 *
	 * @param url      URL
	 * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 */
	public static URI toURI(URL url, boolean isEncode) throws UtilException {
		if (null == url) {
			return null;
		}

		return toURI(url.toString(), isEncode);
	}

	/**
	 * 转字符串为URI
	 *
	 * @param location 字符串路径
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 */
	public static URI toURI(String location) throws UtilException {
		return toURI(location, false);
	}

	/**
	 * 转字符串为URI
	 *
	 * @param location 字符串路径
	 * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 */
	public static URI toURI(String location, boolean isEncode) throws UtilException {
		if (isEncode) {
			location = encode(location);
		}
		try {
			return new URI(StrUtil.trim(location));
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encode(String url) throws UtilException {
		return encode(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 */
	public static String encode(String url, Charset charset) {
		return RFC3986.PATH.encode(url, charset);
	}
}
