package com.yyxnb.android.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * FileIOUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class FileIoUtil {

	private FileIoUtil() {
	}

	public static void closeIo(final Closeable... closeables) {
		if (closeables == null) {
			return;
		}
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					UtilInner.e(e);
				}
			}
		}
	}

}
