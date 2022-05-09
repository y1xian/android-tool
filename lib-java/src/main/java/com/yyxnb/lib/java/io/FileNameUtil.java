package com.yyxnb.lib.java.io;

import com.yyxnb.lib.java.CharUtil;
import com.yyxnb.lib.java.StrUtil;

import java.io.File;

/**
 * 文件名相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
public class FileNameUtil {

	/**
	 * 类Unix路径分隔符
	 */
	public static final char UNIX_SEPARATOR = CharUtil.SLASH;
	/**
	 * Windows路径分隔符
	 */
	public static final char WINDOWS_SEPARATOR = CharUtil.BACKSLASH;

	/**
	 * 返回文件名
	 *
	 * @param file 文件
	 * @return 文件名
	 */
	public static String getName(File file) {
		return (null != file) ? file.getName() : null;
	}

	/**
	 * 返回文件名<br>
	 * <pre>
	 * "/test/aaa" 返回 "aaa"
	 * "/test/aaa.jpg" 返回 "aaa.jpg"
	 * </pre>
	 *
	 * @param filePath 文件
	 * @return 文件名
	 */
	public static String getName(String filePath) {
		if (null == filePath) {
			return null;
		}
		int len = filePath.length();
		if (0 == len) {
			return filePath;
		}
		if (CharUtil.isFileSeparator(filePath.charAt(len - 1))) {
			// 以分隔符结尾的去掉结尾分隔符
			len--;
		}

		int begin = 0;
		char c;
		for (int i = len - 1; i > -1; i--) {
			c = filePath.charAt(i);
			if (CharUtil.isFileSeparator(c)) {
				// 查找最后一个路径分隔符（/或者\）
				begin = i + 1;
				break;
			}
		}

		return filePath.substring(begin, len);
	}

	/**
	 * 获取文件后缀名，扩展名不带“.”
	 * <pre>
	 * "/test/aaa.jpg" 返回 "jpg"
	 * </pre>
	 *
	 * @param file 文件
	 * @return 扩展名
	 * @see #extName(File)
	 */
	public static String getSuffix(File file) {
		return extName(file);
	}

	/**
	 * 获得文件后缀名，扩展名不带“.”
	 *
	 * @param fileName 文件名
	 * @return 扩展名
	 * @see #extName(String)
	 */
	public static String getSuffix(String fileName) {
		return extName(fileName);
	}

	/**
	 * 返回主文件名，无后缀
	 * <pre>
	 * "/test/aaa.jpg" 返回 "aaa"
	 * </pre>
	 *
	 * @param file 文件
	 * @return 主文件名
	 * @see #mainName(File)
	 */
	public static String getPrefix(File file) {
		return mainName(file);
	}

	/**
	 * 返回主文件名，无后缀
	 *
	 * @param fileName 完整文件名
	 * @return 主文件名
	 * @see #mainName(String)
	 */
	public static String getPrefix(String fileName) {
		return mainName(fileName);
	}

	/**
	 * 返回主文件名，无后缀
	 *
	 * @param file 文件
	 * @return 主文件名
	 */
	public static String mainName(File file) {
		if (file.isDirectory()) {
			return file.getName();
		}
		return mainName(file.getName());
	}

	/**
	 * 返回主文件名，无后缀
	 *
	 * @param fileName 完整文件名
	 * @return 主文件名
	 */
	public static String mainName(String fileName) {
		if (null == fileName) {
			return null;
		}
		int len = fileName.length();
		if (0 == len) {
			return fileName;
		}
		if (CharUtil.isFileSeparator(fileName.charAt(len - 1))) {
			len--;
		}

		int begin = 0;
		int end = len;
		char c;
		for (int i = len - 1; i >= 0; i--) {
			c = fileName.charAt(i);
			if (len == end && CharUtil.DOT == c) {
				// 查找最后一个文件名和扩展名的分隔符：.
				end = i;
			}
			// 查找最后一个路径分隔符（/或者\），如果这个分隔符在.之后，则继续查找，否则结束
			if (CharUtil.isFileSeparator(c)) {
				begin = i + 1;
				break;
			}
		}

		return fileName.substring(begin, end);
	}

	/**
	 * 获取文件扩展名（后缀名），扩展名不带“.”
	 *
	 * @param file 文件
	 * @return 扩展名
	 */
	public static String extName(File file) {
		if (null == file) {
			return null;
		}
		if (file.isDirectory()) {
			return null;
		}
		return extName(file.getName());
	}

	/**
	 * 获得文件的扩展名（后缀名），扩展名不带“.”
	 *
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String extName(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf(CharUtil.DOT);
		if (index == -1) {
			return StrUtil.EMPTY;
		} else {
			String ext = fileName.substring(index + 1);
			// 扩展名中不能包含路径相关的符号
			return StrUtil.containsAny(ext, UNIX_SEPARATOR, WINDOWS_SEPARATOR) ? StrUtil.EMPTY : ext;
		}
	}

	/**
	 * 根据文件名检查文件类型，忽略大小写
	 *
	 * @param fileName 文件名，例如xxx.png
	 * @param extNames 被检查的扩展名数组，同一文件类型可能有多种扩展名，扩展名不带“.”
	 * @return 是否是指定扩展名的类型
	 */
	public static boolean isType(String fileName, String... extNames) {
		return StrUtil.equalsAnyIgnoreCase(extName(fileName), extNames);
	}

}
