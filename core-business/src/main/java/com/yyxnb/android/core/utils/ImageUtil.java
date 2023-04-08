package com.yyxnb.android.core.utils;

import android.graphics.Bitmap;

/**
 * ImageUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/12
 */
public class ImageUtil {

	private ImageUtil() {
	}

	private static boolean isJPEG(final byte[] b) {
		return b.length >= 2
				&& (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(final byte[] b) {
		return b.length >= 6
				&& b[0] == 'G' && b[1] == 'I'
				&& b[2] == 'F' && b[3] == '8'
				&& (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	private static boolean isPNG(final byte[] b) {
		return b.length >= 8
				&& (b[0] == (byte) 137 && b[1] == (byte) 80
				&& b[2] == (byte) 78 && b[3] == (byte) 71
				&& b[4] == (byte) 13 && b[5] == (byte) 10
				&& b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(final byte[] b) {
		return b.length >= 2
				&& (b[0] == 0x42) && (b[1] == 0x4d);
	}

	private static boolean isEmptyBitmap(final Bitmap src) {
		return src == null || src.getWidth() == 0 || src.getHeight() == 0;
	}
}
