package com.yyxnb.android.core.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * ConvertUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class ConvertUtil {

	private static final int BUFFER_SIZE = 8192;
	private static final char[] HEX_DIGITS_UPPER =
			{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static final char[] HEX_DIGITS_LOWER =
			{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	private ConvertUtil() {
	}

	public static String int2HexString(int num) {
		return Integer.toHexString(num);
	}

	public static int hexString2Int(String hexString) {
		return Integer.parseInt(hexString, 16);
	}

	public static String bytes2Bits(final byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			for (int j = 7; j >= 0; --j) {
				sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
			}
		}
		return sb.toString();
	}

	public static byte[] bits2Bytes(String bits) {
		int lenMod = bits.length() % 8;
		int byteLen = bits.length() / 8;
		if (lenMod != 0) {
			StringBuilder bitsBuilder = new StringBuilder(bits);
			for (int i = lenMod; i < 8; i++) {
				bitsBuilder.insert(0, "0");
			}
			bits = bitsBuilder.toString();
			byteLen++;
		}
		byte[] bytes = new byte[byteLen];
		for (int i = 0; i < byteLen; ++i) {
			for (int j = 0; j < 8; ++j) {
				bytes[i] <<= 1;
				bytes[i] |= bits.charAt(i * 8 + j) - '0';
			}
		}
		return bytes;
	}

	public static char[] bytes2Chars(final byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		int len = bytes.length;
		if (len <= 0) {
			return null;
		}
		char[] chars = new char[len];
		for (int i = 0; i < len; i++) {
			chars[i] = (char) (bytes[i] & 0xff);
		}
		return chars;
	}

	public static byte[] chars2Bytes(final char[] chars) {
		if (chars == null || chars.length <= 0) {
			return null;
		}
		int len = chars.length;
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			bytes[i] = (byte) (chars[i]);
		}
		return bytes;
	}

	public static String bytes2HexString(final byte[] bytes) {
		return bytes2HexString(bytes, true);
	}

	public static String bytes2HexString(final byte[] bytes, boolean isUpperCase) {
		if (bytes == null) {
			return "";
		}
		char[] hexDigits = isUpperCase ? HEX_DIGITS_UPPER : HEX_DIGITS_LOWER;
		int len = bytes.length;
		if (len <= 0) {
			return "";
		}
		char[] ret = new char[len << 1];
		for (int i = 0, j = 0; i < len; i++) {
			ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
			ret[j++] = hexDigits[bytes[i] & 0x0f];
		}
		return new String(ret);
	}

	public static byte[] hexString2Bytes(String hexString) {
		if (UtilInner.isSpace(hexString)) {
			return new byte[0];
		}
		int len = hexString.length();
		if (len % 2 != 0) {
			hexString = "0" + hexString;
			len = len + 1;
		}
		char[] hexBytes = hexString.toUpperCase().toCharArray();
		byte[] ret = new byte[len >> 1];
		for (int i = 0; i < len; i += 2) {
			ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
		}
		return ret;
	}

	private static int hex2Dec(final char hexChar) {
		if (hexChar >= '0' && hexChar <= '9') {
			return hexChar - '0';
		} else if (hexChar >= 'A' && hexChar <= 'F') {
			return hexChar - 'A' + 10;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static String bytes2String(final byte[] bytes) {
		return bytes2String(bytes, "");
	}

	public static String bytes2String(final byte[] bytes, final String charsetName) {
		if (bytes == null) {
			return null;
		}
		try {
			return new String(bytes, getSafeCharset(charsetName));
		} catch (UnsupportedEncodingException e) {
			UtilInner.e(e);
			return new String(bytes);
		}
	}

	private static String getSafeCharset(String charsetName) {
		String cn = charsetName;
		if (UtilInner.isSpace(charsetName) || !Charset.isSupported(charsetName)) {
			cn = "UTF-8";
		}
		return cn;
	}
}
