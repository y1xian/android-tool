package com.yyxnb.android.core.file;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yyxnb.android.UtilException;
import com.yyxnb.android.core.utils.IOUtil;
import com.yyxnb.android.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 安全的压缩包解压接口
 * 漏洞参考：https://jaq.alibaba.com/community/art/show?articleid=383
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class ZipUtil {

	private static final String TAG = ZipUtil.class.getSimpleName();

	/**
	 * 最大解压大小100M
	 */
	private static final int TOP_SIZE = 100 * 1024 * 1024;

	/**
	 * 最大解压个数,100个文件
	 */
	private static final int TOP_FILE_NUM = 100;
	private static final int BUFFER = 4096;
	private static final String INVALID_STR = "..";
	private static final String[] INVALID_STRS = {"..\\", "../", "./", ".\\.\\", "%00", "..%2F", "..%5C", ".%2F"};

	/**
	 * zip解压缩，默认限制100M大小,100个文件，如果超过100M或者解压文件超过100个，则解压失败
	 *
	 * @param zipFile     zip压缩包文件（含路径，如：/data/data/com.huawei.aegis/files/test.zip）
	 * @param targetDir   指定解压到哪个路径（如：/data/data/com.huawei.aegis/files/test，最好不要以'/'结尾。如果路径下已经存在文件夹或者文件，则先删除已存在的文件夹或者文件）
	 * @param isDeleteOld 如果待解压的文件在已有目录已存在，是否删除原文件，传入true表示删除原文件，false表示保留原文件
	 * @return true表示解压成功，false表示解压失败
	 * @throws UtilException
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Deprecated
	public static boolean unZip(String zipFile, String targetDir, boolean isDeleteOld) throws UtilException {
		return unZip(zipFile, targetDir, TOP_SIZE, TOP_FILE_NUM, isDeleteOld);
	}

	/**
	 * zip解压缩
	 *
	 * @param zipFile        解压文件
	 * @param targetDir      解压路径
	 * @param topSize        指定解压最大大小
	 * @param topFileNumbers 指定解压最大个数
	 * @param isDeleteOld    如果待解压的文件在已有目录已存在，是否删除原文件，传入true表示删除原文件，false表示保留原文件
	 * @return 解压结果，true表示解压成功，false表示解压失败
	 * @throws UtilException
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Deprecated
	public static boolean unZip(String zipFile, String targetDir, long topSize, int topFileNumbers, boolean isDeleteOld)
			throws UtilException {
		if (!isFileOrDirSafe(zipFile, targetDir, topSize, topFileNumbers)) {
			return false;
		}
		if (targetDir.endsWith(File.separator) && targetDir.length() > File.separator.length()) {
			targetDir = targetDir.substring(0, targetDir.length() - File.separator.length());
		}

		boolean result = true;
		byte[] buf = new byte[BUFFER];
		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		ZipEntry entry;
		List<File> listFile = new ArrayList<>();
		try {
			fis = new FileInputStream(zipFile);
			zis = new ZipInputStream(new BufferedInputStream(fis));
			int curTotalSize = 0;
			while ((entry = zis.getNextEntry()) != null) {
				String entryName = entry.getName();
				entryName = entryName.replaceAll("\\\\", "/");
				File file = new File(targetDir, entryName);
				// 判断文件名是否包含../
				String canonicalEntryName = Normalizer.normalize(entryName, Normalizer.Form.NFKC);
				if (isContainInvalidStr(canonicalEntryName)) {
					Log.e(TAG, "zipPath is a invalid path: " + getFileName(canonicalEntryName));
					result = false;
					break;
				}
				if (isDeleteOld && file.exists() && file.isFile()) {
					recursionDeleteFile(file);
				}
				if (entry.isDirectory()) {
					mkdirs(file);
					listFile.add(file);
				} else {
					File parent = file.getParentFile();
					if (parent != null && !parent.exists()) {
						mkdirs(parent);
					}
					fos = new FileOutputStream(file);
					bos = new BufferedOutputStream(fos);
					int len;
					while (((len = zis.read(buf, 0, BUFFER)) != -1)) {
						curTotalSize += len;
						if (curTotalSize > topSize) {
							Log.e(TAG, "unzip  over than top size");
							result = false;
							break;
						}
						bos.write(buf, 0, len);
					}
					listFile.add(file);
					bos.flush();
					IOUtil.closeSecure(bos);
					IOUtil.closeSecure(fos);
				}
				zis.closeEntry();
			}
			IOUtil.closeSecure(zis);
			IOUtil.closeSecure(fis);
		} catch (IOException e) {
			result = false;
			LogUtil.e(TAG, "Unzip IOException : " + e.getMessage());
		} finally {
			closeStream(fis, bos, zis, fos);
		}
		if (!result) {
			unZipFailDelete(listFile);
		}
		return result;
	}

	/**
	 * unzipFile
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static List<File> unZipNew(String zipFilePath, String destDirPath, boolean isDeleteOld)
			throws UtilException {
		return unZipNew(zipFilePath, destDirPath, TOP_SIZE, TOP_FILE_NUM, isDeleteOld);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public static List<File> unZipNew(String zipFile, String targetDir, long topSize, int topFileNumbers,
									  boolean isDeleteOld) throws UtilException {
		if (!isFileOrDirSafe(zipFile, targetDir, topSize, topFileNumbers)) {
			return null;
		}
		if (targetDir.endsWith(File.separator) && targetDir.length() > File.separator.length()) {
			targetDir = targetDir.substring(0, targetDir.length() - File.separator.length());
		}
		return unZipNew(getFileByPath(zipFile), getFileByPath(targetDir), topSize, isDeleteOld, false);
	}

	@SuppressLint("NewApi")
	private static List<File> unZipNew(File zipFile, File destDir, long topSize, boolean isDeleteOld,
									   boolean isGbkZipFile) {
		if (zipFile == null || destDir == null) {
			return null;
		}

		// 是否解压成功 默认为true
		boolean isUnzipSuccess = true;
		int curTotalSize = 0;
		List<File> files = new ArrayList<>();
		ZipFile zf = null;
		try {
			if (!isGbkZipFile) {
				zf = new ZipFile(zipFile);
			} else {
				LogUtil.i(TAG, "not a utf8 zip file, use gbk open zip file : " + zipFile);
				zf = new ZipFile(zipFile, Charset.forName("GBK"));
			}
			Enumeration<?> entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry;
				try {
					entry = (ZipEntry) entries.nextElement();
				} catch (IllegalArgumentException e) {
					LogUtil.i(TAG, "not a utf8 zip file, IllegalArgumentException : " + e.getMessage());
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						return unZipNew(zipFile, destDir, topSize, isDeleteOld, true);
					} else {
						LogUtil.e(TAG, "File is not a utf8 zip file and Build.VERSION_CODES < 24");
						continue;
					}

				}
				String entryName = entry.getName();
				if (TextUtils.isEmpty(entryName)) {
					continue;
				}
				// 判断文件名是否包含../
				String canonicalEntryName = Normalizer.normalize(entryName, Normalizer.Form.NFKC);
				if (isContainInvalidStr(canonicalEntryName)) {
					Log.e(TAG, "zipPath is a invalid path: " + getFileName(canonicalEntryName));
					isUnzipSuccess = false;
					break;
				}

				// 将 \ 替换成 /
				canonicalEntryName = canonicalEntryName.replaceAll("\\\\", "/");
				File file = new File(destDir, canonicalEntryName);
				if (isDeleteOld && file.exists() && file.isFile()) {
					recursionDeleteFile(file);
				}
				files.add(file);
				if (entry.isDirectory()) {
					if (!createOrExistsDir(file)) {
						isUnzipSuccess = false;
						return null;
					}
				} else {
					if (!createOrExistsFile(file)) {
						isUnzipSuccess = false;
						return null;
					}
					InputStream in = null;
					OutputStream out = null;
					FileOutputStream fileOut = null;
					try {
						in = new BufferedInputStream(zf.getInputStream(entry));
						fileOut = new FileOutputStream(file);
						out = new BufferedOutputStream(fileOut);
						byte[] buffer = new byte[1024];
						int len;
						while ((len = in.read(buffer)) != -1) {
							curTotalSize += len;
							if (curTotalSize > topSize) {
								Log.e(TAG, "unzipFileNew: over than top size");
								isUnzipSuccess = false;
								break;
							}
							out.write(buffer, 0, len);
						}
					} finally {
						IOUtil.closeSecure(in);
						IOUtil.closeSecure(out);
						IOUtil.closeSecure(fileOut);
					}
				}
			}

		} catch (IOException e) {
			isUnzipSuccess = false;
			Log.e(TAG, "unzip new IOException : " + e.getMessage());
		} finally {
			IOUtil.closeSecure(zf);
			if (!isUnzipSuccess) {
				unZipFailDelete(files);
			}
		}
		return files;
	}

	/**
	 * 获取文件名，带后缀
	 *
	 * @param filePath 文件路径
	 * @return 文件名，带后缀
	 */
	private static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}
		int lastSep = filePath.lastIndexOf(File.separator);
		return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
	}

	/**
	 * 创建文件，如果存在则直接返回true
	 *
	 * @param file 文件
	 * @return 返回是否存在文件
	 */
	private static boolean createOrExistsFile(File file) {
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return file.isFile();
		}
		if (!createOrExistsDir(file.getParentFile())) {
			return false;
		}
		try {
			return file.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, "createOrExistsFile IOException ");
			return false;
		}
	}

	private static void checkPathIsSafeCheck(String path) {
		if (!TextUtils.isEmpty(path)) {
			// 如果路径跨目录,抛异常
			if (isContainInvalidStr(path)) {
				Log.e(TAG, "IllegalArgumentException--path is not a standard path");
				throw new IllegalArgumentException("path is not a standard path");
			}
		}

	}

	/**
	 * 创建文件夹，如果存在则直接返回true
	 *
	 * @param file 文件夹
	 * @return 返回是否存在文件夹
	 */
	private static boolean createOrExistsDir(File file) {
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	/**
	 * 通过文件路径返回文件
	 *
	 * @param filePath 文件路径
	 * @return 文件
	 */
	private static File getFileByPath(String filePath) {
		return TextUtils.isEmpty(filePath) ? null : getFile(filePath);
	}

	/**
	 * package getFile
	 */
	private static File getFile(String path) {
		checkPathIsSafeCheck(path);
		return new File(path);
	}

	/**
	 * 读取zip包中所有的文件名是否包含"../"以及判断压缩包内的文件数量，如果包含则"../"或者文件数量大于topFileNumbers，或者解压后总文件大小超过topSize，则说明是恶意文件，则不解压
	 * 参考：https://jaq.alibaba.com/community/art/show?articleid=383
	 *
	 * @param filePath
	 * @return
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	private static boolean isZipFileValid(String filePath, long topSize, int topFileNumbers, boolean isGbkZipFile) {
		boolean isValid = true;
		ZipFile zipFile = null;
		/*
		 * 压缩包内包含的文件数目
		 */
		int fileNumbers = 0;

		/*
		 * 解压后的总大小
		 */
		long totalSize = 0L;

		/*
		 * 当前文件的大小
		 */
		long currentEntrySize = 0L;
		try {
			if (!isGbkZipFile) {
				zipFile = new ZipFile(filePath);
			} else {
				LogUtil.i(TAG, "not a utf8 zip file, use gbk open zip file : " + filePath);
				zipFile = new ZipFile(filePath, Charset.forName("GBK"));
			}
			Enumeration<ZipEntry> entrys = (Enumeration<ZipEntry>) zipFile.entries();
			ZipEntry zipEntry;

			while (entrys.hasMoreElements()) {
				try {
					zipEntry = entrys.nextElement();
				} catch (IllegalArgumentException e) {
					LogUtil.i(TAG, "not a utf8 zip file, IllegalArgumentException : " + e.getMessage());
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						isValid = isZipFileValid(filePath, topSize, topFileNumbers, true);
						break;
					} else {
						LogUtil.e(TAG, "File is not a utf8 zip file and Build.VERSION_CODES < 24");
						isValid = false;
						continue;
					}
				}
				currentEntrySize = zipEntry.getSize();
				totalSize += currentEntrySize;
				fileNumbers++;
				if (isContainInvalidStr(zipEntry.getName()) || fileNumbers >= topFileNumbers || totalSize > topSize
						|| zipEntry.getSize() == -1) {
					LogUtil.e(TAG, "File name is invalid or too many files or too big");
					isValid = false;
					break;
				}
			}
		} catch (IOException e) {
			isValid = false;
			LogUtil.e(TAG, "not a valid zip file, IOException : " + e.getMessage());
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					LogUtil.e(TAG, "close zipFile IOException ");
				}
			}
		}
		return isValid;
	}

	/**
	 * 递归删除一个文件（目录）下的所有文件
	 *
	 * @param file
	 */
	private static void recursionDeleteFile(File file) {
		if (file == null) {
			return;
		}
		if (file.isFile()) {
			deleteFile(file);
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				deleteFile(file);
				return;
			}
			for (File f : childFile) {
				recursionDeleteFile(f);
			}
			deleteFile(file);
		}
	}

	/**
	 * 1、判断压缩文件或者解压路径是否合法，判断解压包里面的文件是否包含恶意字符，避免跨目录遍历漏洞，
	 * 2、判断文件个数是否超过给定的值fileNumbers
	 * 3、判断解压后总大小是否超过给定的值topSize
	 * 任何一个不满足都不解压，返回失败
	 *
	 * @param zipFile
	 * @param targetDir
	 * @return
	 * @throws UtilException
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	private static boolean isFileOrDirSafe(String zipFile, String targetDir, long topSize, int topFileNumbers)
			throws UtilException {
		if (TextUtils.isEmpty(zipFile) || isContainInvalidStr(zipFile)) {
			LogUtil.e(TAG, "zip file is not valid");
			return false;
		}
		if (TextUtils.isEmpty(targetDir) || isContainInvalidStr(targetDir)) {
			LogUtil.e(TAG, "target directory is not valid");
			return false;
		}
		if (!isZipFileValid(zipFile, topSize, topFileNumbers, false)) {
			LogUtil.e(TAG, "zip file contains valid chars or too many files");
			throw new UtilException("unsecure zipfile!");
		}
		return true;
	}

	/**
	 * 如果解压失败，则删除已解压出来的文件
	 *
	 * @param listFile 已解压文件列表
	 * @return 删除成功还是失败
	 */
	private static boolean unZipFailDelete(List<File> listFile) {
		try {
			for (File file : listFile) {
				recursionDeleteFile(file);
			}
			return true;
		} catch (Exception e) {
			LogUtil.e(TAG, "unzip fail delete file failed" + e.getMessage());
			return false;
		}
	}

	/**
	 * 安全关闭流文件
	 *
	 * @param fis
	 * @param dest
	 * @param zis
	 * @param fos
	 */
	private static void closeStream(FileInputStream fis, BufferedOutputStream dest, ZipInputStream zis,
									FileOutputStream fos) {
		IOUtil.closeSecure(fis);
		IOUtil.closeSecure(dest);
		IOUtil.closeSecure(zis);
		IOUtil.closeSecure(fos);
	}

	private static void deleteFile(File file) {
		if (file == null) {
			return;
		}
		if (!file.delete()) {
			LogUtil.e(TAG, "delete file error");
		}
	}

	private static void mkdirs(File file) {
		if (file == null || file.exists()) {
			return;
		}
		if (!file.mkdirs()) {
			LogUtil.e(TAG, "mkdirs error , files exists or IOException.");
		}
	}

	/**
	 * 检查文件名是否包含危险字符，如果文件名为空则不校验，调用方法都已对str判空
	 *
	 * @param str 文件名
	 * @return 判断结果
	 */
	private static boolean isContainInvalidStr(String str) {
		if (TextUtils.isEmpty(str)) {
			Log.e(TAG, "isContainInvalidStr: name is null");
			return false;
		}
		if (str.equals(INVALID_STR)) {
			return true;
		}
		for (String s : INVALID_STRS) {
			if (str.contains(s)) {
				return true;
			}
		}
		return false;
	}
}