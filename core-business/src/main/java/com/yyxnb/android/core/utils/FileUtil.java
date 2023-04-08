package com.yyxnb.android.core.utils;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * FileUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/11
 */
public class FileUtil {

	/**
	 * 文件路径分隔符<br>
	 */
	public static final String FILE_SEPARATOR = File.separator;

	/**
	 * 获取当前系统的换行分隔符
	 *
	 * <pre>
	 * Windows: \r\n
	 * Mac: \r
	 * Linux: \n
	 * </pre>
	 *
	 * @return 换行符
	 */
	public static String getLineSeparator() {
		return System.lineSeparator();
	}

	private FileUtil() {
	}

	private static String getAbsolutePath(final File file) {
		if (file == null) {
			return "";
		}
		return file.getAbsolutePath();
	}

	/**
	 * 获取规范的绝对路径
	 *
	 * @param file 文件
	 * @return 规范绝对路径，如果传入file为null，返回null
	 */
	public static String getCanonicalPath(File file) {
		if (null == file) {
			return null;
		}
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static File getFileByPath(final String filePath) {
		return UtilInner.isSpace(filePath) ? null : new File(filePath);
	}

	public static boolean isFileExists(final File file) {
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
		return isFileExists(file.getAbsolutePath());
	}

	public static boolean isFileExists(final String filePath) {
		File file = getFileByPath(filePath);
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
		return isFileExistsApi29(filePath);
	}

	private static boolean isFileExistsApi29(String filePath) {
		if (Build.VERSION.SDK_INT >= 29) {
			try {
				Uri uri = Uri.parse(filePath);
				ContentResolver cr = UtilInner.getApp().getContentResolver();
				AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
				if (afd == null) {
					return false;
				}
				try {
					afd.close();
				} catch (IOException ignore) {
				}
			} catch (FileNotFoundException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isDir(final String dirPath) {
		return isDir(getFileByPath(dirPath));
	}


	public static boolean isDir(final File file) {
		return file != null && file.exists() && file.isDirectory();
	}

	public static boolean isFile(final String filePath) {
		return isFile(getFileByPath(filePath));
	}

	public static boolean isFile(final File file) {
		return file != null && file.exists() && file.isFile();
	}

	public static boolean delete(final String filePath) {
		return delete(getFileByPath(filePath));
	}

	/**
	 * Delete the directory.
	 *
	 * @param file The file.
	 * @return {@code true}: success<br>{@code false}: fail
	 */
	public static boolean delete(final File file) {
		if (file == null) {
			return false;
		}
		if (file.isDirectory()) {
			return deleteDir(file);
		}
		return deleteFile(file);
	}

	private static boolean deleteDir(final File dir) {
		if (dir == null) {
			return false;
		}
		// dir doesn't exist then return true
		if (!dir.exists()) {
			return true;
		}
		// dir isn't a directory then return false
		if (!dir.isDirectory()) {
			return false;
		}
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isFile()) {
					if (!file.delete()) {
						return false;
					}
				} else if (file.isDirectory()) {
					if (!deleteDir(file)) {
						return false;
					}
				}
			}
		}
		return dir.delete();
	}

	private static boolean deleteFile(final File file) {
		return file != null && (!file.exists() || file.isFile() && file.delete());
	}

	public static List<File> listFilesInDir(final File dir,
											final boolean isRecursive,
											final Comparator<File> comparator) {
		return listFilesInDirWithFilter(dir, pathname -> true, isRecursive, comparator);
	}

	public static List<File> listFilesInDirWithFilter(final File dir,
													  final FileFilter filter,
													  final boolean isRecursive,
													  final Comparator<File> comparator) {
		List<File> files = listFilesInDirWithFilterInner(dir, filter, isRecursive);
		if (comparator != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				files.sort(comparator);
			}
		}
		return files;
	}

	private static List<File> listFilesInDirWithFilterInner(final File dir,
															final FileFilter filter,
															final boolean isRecursive) {
		List<File> list = new ArrayList<>();
		if (!isDir(dir)) {
			return list;
		}
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (filter.accept(file)) {
					list.add(file);
				}
				if (isRecursive && file.isDirectory()) {
					list.addAll(listFilesInDirWithFilterInner(file, filter, true));
				}
			}
		}
		return list;
	}
}
