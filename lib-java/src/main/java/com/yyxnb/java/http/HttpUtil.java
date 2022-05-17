package com.yyxnb.java.http;

import java.util.regex.Pattern;

/**
 * Http请求工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/27
 */
public class HttpUtil {

	/**
	 * 正则：Content-Type中的编码信息
	 */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
	/**
	 * 正则：匹配meta标签的编码信息
	 */
	public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	/**
	 * 检测是否https
	 *
	 * @param url URL
	 * @return 是否https
	 */
	public static boolean isHttps(String url) {
		return url.toLowerCase().startsWith("https:");
	}

	/**
	 * 检测是否http
	 *
	 * @param url URL
	 * @return 是否http
	 */
	public static boolean isHttp(String url) {
		return url.toLowerCase().startsWith("http:");
	}



}
