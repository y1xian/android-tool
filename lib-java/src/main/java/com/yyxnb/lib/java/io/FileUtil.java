package com.yyxnb.lib.java.io;

import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.collection.ArrayUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
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

	/**
	 * 判断是否为目录，如果path为null，则返回false
	 *
	 * @param path 文件路径
	 * @return 如果为目录true
	 */
	public static boolean isDirectory(String path) {
		return (null != path) && file(path).isDirectory();
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false
	 *
	 * @param file 文件
	 * @return 如果为目录true
	 */
	public static boolean isDirectory(File file) {
		return (null != file) && file.isDirectory();
	}

	/**
	 * 判断是否为文件，如果path为null，则返回false
	 *
	 * @param path 文件路径
	 * @return 如果为文件true
	 */
	public static boolean isFile(String path) {
		return (null != path) && file(path).isFile();
	}

	/**
	 * 判断是否为文件，如果file为null，则返回false
	 *
	 * @param file 文件
	 * @return 如果为文件true
	 */
	public static boolean isFile(File file) {
		return (null != file) && file.isFile();
	}

	/**
	 * 检查是否存在某个文件
	 *
	 * @param file 文件
	 * @return {@code true} yes, {@code false} no
	 */
	public static boolean isFileExists(final File file) {
		return file != null && file.exists();
	}

	/**
	 * 检查是否存在某个文件
	 *
	 * @param filePath 文件路径
	 * @return {@code true} yes, {@code false} no
	 */
	public static boolean isFileExists(final String filePath) {
		return isFileExists(getFileByPath(filePath));
	}

	/**
	 * 检查是否存在某个文件
	 *
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return {@code true} yes, {@code false} no
	 */
	public static boolean isFileExists(
			final String filePath,
			final String fileName
	) {
		return filePath != null && fileName != null && new File(filePath, fileName).exists();
	}


	/**
	 * 列出指定路径下的目录和文件<br>
	 * 给定的绝对路径不能是压缩包中的路径
	 *
	 * @param path 目录绝对路径或者相对路径
	 * @return 文件列表（包含目录）
	 */
	public static File[] ls(String path) {
		if (path == null) {
			return null;
		}

		File file = file(path);
		if (file.isDirectory()) {
			return file.listFiles();
		}
		throw new IORuntimeException(StrUtil.format("Path %s is not directory!", path));
	}

	/**
	 * 创建File对象，相当于调用new File()，不做任何处理
	 *
	 * @param path 文件路径，相对路径表示相对项目路径
	 * @return File
	 */
	public static File newFile(String path) {
		return new File(path);
	}

	/**
	 * 创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
	 *
	 * @param path 相对ClassPath的目录或者绝对路径目录
	 * @return File
	 */
	public static File file(String path) {
		if (null == path) {
			return null;
		}
		return new File(getAbsolutePath(new File(path)));
	}

	/**
	 * 创建文件夹，会递归自动创建其不存在的父文件夹，如果存在直接返回此文件夹<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 *
	 * @param dir 目录
	 * @return 创建的目录
	 */
	public static File mkdir(File dir) {
		if (dir == null) {
			return null;
		}
		if (!dir.exists()) {
			//noinspection ResultOfMethodCallIgnored
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 创建所给文件或目录的父目录
	 *
	 * @param file 文件或目录
	 * @return 父目录
	 */
	public static File mkParentDirs(File file) {
		if (null == file) {
			return null;
		}
		return mkdir(file.getParentFile());
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
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取标准的绝对路径
	 *
	 * @param file 文件
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(File file) {
		if (file == null) {
			return null;
		}

		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}

	/**
	 * 获取文件
	 *
	 * @param filePath 文件路径
	 * @return 文件 {@link File}
	 */
	public static File getFileByPath(final String filePath) {
		return filePath != null ? new File(filePath) : null;
	}

	/**
	 * 判断文件是否存在，如果path为null，则返回false
	 *
	 * @param path 文件路径
	 * @return 如果存在返回true
	 */
	public static boolean exist(String path) {
		return (null != path) && file(path).exists();
	}

	/**
	 * 判断文件是否存在，如果file为null，则返回false
	 *
	 * @param file 文件
	 * @return 如果存在返回true
	 */
	public static boolean exist(File file) {
		return (null != file) && file.exists();
	}

	/**
	 * 是否存在匹配文件
	 *
	 * @param directory 文件夹路径
	 * @param regexp    文件夹中所包含文件名的正则表达式
	 * @return 如果存在匹配文件返回true
	 */
	public static boolean exist(String directory, String regexp) {
		final File file = new File(directory);
		if (!file.exists()) {
			return false;
		}

		final String[] fileList = file.list();
		if (fileList == null) {
			return false;
		}

		for (String fileName : fileList) {
			if (fileName.matches(regexp)) {
				return true;
			}

		}
		return false;
	}

	/**
	 * 指定文件最后修改时间
	 *
	 * @param file 文件
	 * @return 最后修改时间
	 */
	public static Date lastModifiedTime(File file) {
		if (!exist(file)) {
			return null;
		}

		return new Date(file.lastModified());
	}

	/**
	 * 指定路径文件最后修改时间
	 *
	 * @param path 绝对路径
	 * @return 最后修改时间
	 */
	public static Date lastModifiedTime(String path) {
		return lastModifiedTime(new File(path));
	}

	/**
	 * 计算目录或文件的总大小<br>
	 * 当给定对象为文件时，直接调用 {@link File#length()}<br>
	 * 当给定对象为目录时，遍历目录下的所有文件和目录，递归计算其大小，求和返回<br>
	 * 此方法不包括目录本身的占用空间大小。
	 *
	 * @param file 目录或文件,null或者文件不存在返回0
	 * @return 总大小，bytes长度
	 */
	public static long size(File file) {
		return size(file, false);
	}

	/**
	 * 计算目录或文件的总大小<br>
	 * 当给定对象为文件时，直接调用 {@link File#length()}<br>
	 * 当给定对象为目录时，遍历目录下的所有文件和目录，递归计算其大小，求和返回
	 *
	 * @param file           目录或文件,null或者文件不存在返回0
	 * @param includeDirSize 是否包括每层目录本身的大小
	 * @return 总大小，bytes长度
	 */
	public static long size(File file, boolean includeDirSize) {
		if (null == file || !file.exists()) {
			return 0;
		}

		if (file.isDirectory()) {
			long size = includeDirSize ? file.length() : 0;
			File[] subFiles = file.listFiles();
			if (ArrayUtil.isEmpty(subFiles)) {
				return 0L;// empty directory
			}
			for (File subFile : subFiles) {
				size += size(subFile, includeDirSize);
			}
			return size;
		} else {
			return file.length();
		}
	}

	/**
	 * 可读的文件大小<br>
	 *
	 * @param size Long类型大小
	 * @return 大小
	 */
	public static String format(long size) {
		if (size <= 0) {
			return "0";
		}
		int digitGroups = Math.min(DataUnit.UNIT_NAMES.length - 1, (int) (Math.log10(size) / Math.log10(1024)));
		return new DecimalFormat("#,##0.##")
				.format(size / Math.pow(1024, digitGroups)) + DataUnit.UNIT_NAMES[digitGroups];
	}

	// ----------------------------------------------------------------------- del

	/**
	 * 删除文件或者文件夹<br>
	 * 路径如果为相对路径，会转换为ClassPath路径！ 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 *
	 * @param fullFileOrDirPath 文件或者目录的路径
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 */
	public static boolean del(String fullFileOrDirPath) throws IORuntimeException {
		return del(file(fullFileOrDirPath));
	}

	/**
	 * 删除文件或者文件夹<br>
	 * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 *
	 * @param file 文件对象
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 */
	public static boolean del(File file) throws IORuntimeException {
		if (file == null || !file.exists()) {
			// 如果文件不存在或已被删除，此处返回true表示删除成功
			return true;
		}
		if (file.isDirectory()) {
			// 清空目录下所有文件和目录
			boolean isOk = clean(file);
			if (!isOk) {
				return false;
			}
		}
		return file.delete();
	}

	/**
	 * 清空文件夹<br>
	 * 注意：清空文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 *
	 * @param dirPath 文件夹路径
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 */
	public static boolean clean(String dirPath) throws IORuntimeException {
		return clean(file(dirPath));
	}

	/**
	 * 清空文件夹<br>
	 * 注意：清空文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 *
	 * @param directory 文件夹
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 */
	public static boolean clean(File directory) throws IORuntimeException {
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			return true;
		}

		final File[] files = directory.listFiles();
		if (null != files) {
			for (File childFile : files) {
				if (!del(childFile)) {
					// 删除一个出错则本次删除任务失败
					return false;
				}
			}
		}
		return true;
	}

	// ----------------------------------------------------------------------- 遍历

	/**
	 * 获取目录下所有文件 ( 不递归进子目录 )
	 *
	 * @param dirPath 目录路径
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(final String dirPath) {
		return listFilesInDir(dirPath, false);
	}

	/**
	 * 获取目录下所有文件 ( 不递归进子目录 )
	 *
	 * @param dir 目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(final File dir) {
		return listFilesInDir(dir, false);
	}

	/**
	 * 获取目录下所有文件
	 *
	 * @param dirPath     目录路径
	 * @param isRecursive 是否递归进子目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(
			final String dirPath,
			final boolean isRecursive
	) {
		return listFilesInDir(getFileByPath(dirPath), isRecursive);
	}

	/**
	 * 获取目录下所有文件
	 *
	 * @param dir         目录
	 * @param isRecursive 是否递归进子目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(
			final File dir,
			final boolean isRecursive
	) {
		return listFilesInDirWithFilter(dir, pathname -> true, isRecursive);
	}

	/**
	 * 获取目录下所有过滤的文件 ( 不递归进子目录 )
	 *
	 * @param dirPath 目录路径
	 * @param filter  过滤器
	 * @return 文件链表
	 */
	public static List<File> listFilesInDirWithFilter(
			final String dirPath,
			final FileFilter filter
	) {
		return listFilesInDirWithFilter(getFileByPath(dirPath), filter, false);
	}

	/**
	 * 获取目录下所有过滤的文件 ( 不递归进子目录 )
	 *
	 * @param dir    目录
	 * @param filter 过滤器
	 * @return 文件链表
	 */
	public static List<File> listFilesInDirWithFilter(
			final File dir,
			final FileFilter filter
	) {
		return listFilesInDirWithFilter(dir, filter, false);
	}

	/**
	 * 获取目录下所有过滤的文件
	 *
	 * @param dirPath     目录路径
	 * @param filter      过滤器
	 * @param isRecursive 是否递归进子目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDirWithFilter(
			final String dirPath,
			final FileFilter filter,
			final boolean isRecursive
	) {
		return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
	}

	/**
	 * 获取目录下所有过滤的文件
	 *
	 * @param dir         目录
	 * @param filter      过滤器
	 * @param isRecursive 是否递归进子目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDirWithFilter(
			final File dir,
			final FileFilter filter,
			final boolean isRecursive
	) {
		if (!isDirectory(dir) || filter == null) {
			return null;
		}
		List<File> list = new ArrayList<>();
		File[] files = dir.listFiles();
		if (files != null && files.length != 0) {
			for (File file : files) {
				if (filter.accept(file)) {
					list.add(file);
				}
				if (isRecursive && file.isDirectory()) {
					List<File> fileLists = listFilesInDirWithFilter(file, filter, true);
					if (fileLists != null) {
						list.addAll(fileLists);
					}
				}
			}
		}
		return list;
	}

	// -----------------------------------------------------------------------

	/**
	 * 复制文件
	 *
	 * @param inputStream 输入流
	 * @param target      目标文件
	 */
	public static void write(InputStream inputStream, File target) throws Exception {
		OutputStream outputStream = null;
		try {
			mkParentDirs(target);
			// 文件输出流并进行缓冲
			outputStream = new BufferedOutputStream(new FileOutputStream(target));
			byte[] bytes = new byte[1024 * 8];
			int length;
			while ((length = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, length);
			}
			// 刷新输出缓冲流
			outputStream.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			IoUtil.close(outputStream);
			IoUtil.close(inputStream);
		}
	}
}
