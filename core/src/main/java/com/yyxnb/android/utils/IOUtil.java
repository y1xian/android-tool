package com.yyxnb.android.utils;

import android.database.Cursor;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * IO操作相关的工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class IOUtil {

	private static final String TAG = IOUtil.class.getSimpleName();

	private static final int BUFF_SIZE = 4 * 1024;

	private IOUtil() {
	}

	/**
	 * 安全关闭Cursor对象
	 *
	 * @param cursor 接受null参数
	 */
	public static void close(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	/**
	 * 安全关闭Reader对象
	 *
	 * @param reader 接受null参数
	 */
	public static void closeSecure(Reader reader) {
		closeSecure((Closeable) reader);
	}

	/**
	 * 安全关闭Writer对象
	 *
	 * @param writer 接受null参数
	 */
	public static void closeSecure(Writer writer) {
		closeSecure((Closeable) writer);
	}

	/**
	 * 安全关闭InputStream对象
	 *
	 * @param input 接受null参数
	 */
	public static void closeSecure(InputStream input) {
		closeSecure((Closeable) input);
	}

	/**
	 * 安全关闭OutputStream对象
	 *
	 * @param output 接受null参数
	 */
	public static void closeSecure(OutputStream output) {
		closeSecure((Closeable) output);
	}

	/**
	 * 安全关闭Closeable对象
	 *
	 * @param closeables 接受null参数, 和已经关闭的.
	 */
	public static void closeSecure(Closeable... closeables) {
		if (closeables != null) {
			try {
				for (Closeable closeable : closeables) {
					closeable.close();
				}
			} catch (IOException e) {
				UtilInner.e(TAG, "closeSecure IOException");
			}
		}
	}

	/**
	 * 从输入流中拷贝指定位数的数据
	 */
	public static long copy(InputStream input, OutputStream output) throws IOException {
		return copy(input, output, new byte[BUFF_SIZE]);
	}

	/**
	 * 从输入流中拷贝到输出流
	 *
	 * @param input
	 * @param output
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		long count = 0L;
		int n;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * 将输入流转化成byte数组
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * byte数据转换为InputStream
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static InputStream toInputStream(byte[] input) throws IOException {
		return new ByteArrayInputStream(input);
	}

	/**
	 * Deletes the file object, which may be a file or a directory.
	 *
	 * @param file the file to delete
	 */
	public static void deleteSecure(File file) {
		if (file != null && file.exists()) {
			if (!file.delete()) {
				UtilInner.e(TAG, "deleteSecure exception");
			}
		}
	}

	/**
	 * Deletes the file object, which may be a file or a directory.
	 *
	 * @param file the file to delete
	 */
	public static void deleteSecure(String file) {
		if (!TextUtils.isEmpty(file)) {
			deleteSecure(new File(file));
		}
	}

}
