package com.yyxnb.android.utils;

import com.yyxnb.android.Oh;
import com.yyxnb.java.StrUtil;
import com.yyxnb.java.io.IoUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 转换工具
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/8
 */
public class ConvertUtil {

	private static final int BUFFER_SIZE = 8192;

	/**
	 * Bytes to bits.
	 *
	 * @param bytes The bytes.
	 * @return bits
	 */
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

	/**
	 * Bits to bytes.
	 *
	 * @param bits The bits.
	 * @return bytes
	 */
	public static byte[] bits2Bytes(String bits) {
		int lenMod = bits.length() % 8;
		int byteLen = bits.length() / 8;
		// add "0" until length to 8 times
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


	/**
	 * 输入流转输出流
	 */
	public static ByteArrayOutputStream input2OutputStream(final InputStream is) {
		if (is == null) {
			return null;
		}
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] b = new byte[BUFFER_SIZE];
			int len;
			while ((len = is.read(b, 0, BUFFER_SIZE)) != -1) {
				os.write(b, 0, len);
			}
			return os;
		} catch (IOException e) {
			Oh.log().e(e, "转换异常");
			return null;
		} finally {
			IoUtil.close(is);
		}
	}

	/**
	 * 输入流转字符串
	 */
	public static String inputStream2String(final InputStream is, final String charsetName) {
		if (is == null) {
			return "";
		}
		try {
			ByteArrayOutputStream baos = input2OutputStream(is);
			if (baos == null) {
				return "";
			}
			return baos.toString(getSafeCharset(charsetName));
		} catch (UnsupportedEncodingException e) {
			Oh.log().e(e, "转换异常");
			return "";
		}
	}

	private static String getSafeCharset(String charsetName) {
		String cn = charsetName;
		if (StrUtil.isSpace(charsetName) || !Charset.isSupported(charsetName)) {
			cn = "UTF-8";
		}
		return cn;
	}
}
