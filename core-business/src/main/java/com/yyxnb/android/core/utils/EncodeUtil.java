package com.yyxnb.android.core.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * EncodeUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class EncodeUtil {

	private EncodeUtil() {
	}

	public static String urlEncode(final String input) {
		return urlEncode(input, "UTF-8");
	}

	public static String urlEncode(final String input, final String charsetName) {
		if (input == null || input.length() == 0) {
			return "";
		}
		try {
			return URLEncoder.encode(input, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	public static String urlDecode(final String input) {
		return urlDecode(input, "UTF-8");
	}

	public static String urlDecode(final String input, final String charsetName) {
		if (input == null || input.length() == 0) {
			return "";
		}
		try {
			String safeInput = input.replaceAll("%(?![0-9a-fA-F]{2})", "%25")
					.replaceAll("\\+", "%2B");
			return URLDecoder.decode(safeInput, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	public static byte[] base64Encode(final String input) {
		return base64Encode(input.getBytes());
	}


	public static byte[] base64Encode(final byte[] input) {
		if (input == null || input.length == 0) {
			return new byte[0];
		}
		return Base64.encode(input, Base64.NO_WRAP);
	}

	public static String base64Encode2String(final byte[] input) {
		if (input == null || input.length == 0) {
			return "";
		}
		return Base64.encodeToString(input, Base64.NO_WRAP);
	}

	public static byte[] base64Decode(final String input) {
		if (input == null || input.length() == 0) {
			return new byte[0];
		}
		return Base64.decode(input, Base64.NO_WRAP);
	}
}
