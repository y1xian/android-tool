package com.yyxnb.java;

import com.yyxnb.java.io.FileUtil;
import com.yyxnb.java.io.IoUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
public class ZipUtil {
	// 缓存大小
	private static final int BUFFER_LEN = 8192;

	// ----------------------------------------------------------------------- 压缩

	/**
	 * 批量压缩文件
	 *
	 * @param resFiles    待压缩文件路径集合
	 * @param zipFilePath 压缩文件路径
	 * @return {@code true} 压缩成功, {@code false} 压缩失败
	 * @throws Exception 异常时抛出
	 */
	public static boolean zipFiles(
			final Collection<String> resFiles,
			final String zipFilePath
	)
			throws Exception {
		return zipFiles(resFiles, zipFilePath, null);
	}

	/**
	 * 批量压缩文件
	 *
	 * @param resFilePaths 待压缩文件路径集合
	 * @param zipFilePath  压缩文件路径
	 * @param comment      压缩文件的注释
	 * @return {@code true} 压缩成功, {@code false} 压缩失败
	 * @throws Exception 异常时抛出
	 */
	public static boolean zipFiles(
			final Collection<String> resFilePaths,
			final String zipFilePath,
			final String comment
	)
			throws Exception {
		if (resFilePaths == null || zipFilePath == null) {
			return false;
		}
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
			for (String resFile : resFilePaths) {
				if (!zipFile(FileUtil.getFileByPath(resFile), "", zos, comment)) {
					return false;
				}
			}
			return true;
		} finally {
			if (zos != null) {
				zos.finish();
				IoUtil.close(zos);
			}
		}
	}

	/**
	 * 压缩文件
	 *
	 * @param resFile  待压缩文件
	 * @param rootPath 相对于压缩文件的路径
	 * @param zos      压缩文件输出流
	 * @param comment  压缩文件的注释
	 * @return {@code true} 压缩成功, {@code false} 压缩失败
	 * @throws Exception 异常时抛出
	 */
	private static boolean zipFile(
			final File resFile,
			final String rootPath,
			final ZipOutputStream zos,
			final String comment
	)
			throws Exception {
		// 处理后的文件路径
		String filePath = rootPath + (StrUtil.isEmpty(rootPath) ? "" : File.separator) + resFile.getName();
		if (resFile.isDirectory()) {
			File[] fileList = resFile.listFiles();
			// 如果是空文件夹那么创建它
			if (fileList == null || fileList.length == 0) {
				ZipEntry entry = new ZipEntry(filePath + '/');
				entry.setComment(comment);
				zos.putNextEntry(entry);
				zos.closeEntry();
			} else {
				for (File file : fileList) {
					// 如果递归返回 false 则返回 false
					if (!zipFile(file, filePath, zos, comment)) {
						return false;
					}
				}
			}
		} else {
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(resFile));
				ZipEntry entry = new ZipEntry(filePath);
				entry.setComment(comment);
				zos.putNextEntry(entry);
				byte[] buffer = new byte[BUFFER_LEN];
				int len;
				while ((len = is.read(buffer, 0, BUFFER_LEN)) != -1) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
			} finally {
				IoUtil.close(is);
			}
		}
		return true;
	}

	/**
	 * 压缩文件和文件夹
	 *
	 * @param srcFileString 要压缩的文件或文件夹
	 * @param zipFileString 解压完成的Zip路径
	 * @throws Exception 异常
	 */
	public static void zipFolder(String srcFileString, String zipFileString) throws Exception {
		//创建ZIP
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
		//创建文件
		File file = new File(srcFileString);
		//压缩
		zipFiles(file.getParent() + File.separator, file.getName(), outZip);
		//完成和关闭
		outZip.finish();
		outZip.close();
	}

	/**
	 * 压缩文件
	 *
	 * @param folderString   文件夹
	 * @param fileString     文件名
	 * @param zipOutputSteam
	 * @throws Exception 异常
	 */
	private static void zipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
		if (zipOutputSteam == null) {
			return;
		}
		File file = new File(folderString + fileString);
		if (file.isFile()) {
			ZipEntry zipEntry = new ZipEntry(fileString);
			FileInputStream inputStream = new FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[4096];
			while ((len = inputStream.read(buffer)) != -1) {
				zipOutputSteam.write(buffer, 0, len);
			}
			zipOutputSteam.closeEntry();
		} else {
			//文件夹
			String[] fileList = file.list();
			//没有子文件和压缩
			assert fileList != null;
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();
			}
			//子文件和递归
			for (String s : fileList) {
				zipFiles(folderString, fileString + File.separator + s, zipOutputSteam);
			}
		}
	}


	// ----------------------------------------------------------------------- 解压

	/**
	 * 解压zip到指定的路径
	 *
	 * @param zipFileString ZIP的名称
	 * @param outPathString 要解压缩路径
	 * @throws Exception 异常
	 */
	public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				//获取部件的文件夹名
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();
			} else {
				File file = new File(outPathString + File.separator + szName);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				// 获取文件的输出流
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// 读取（字节）字节到缓冲区
				while ((len = inZip.read(buffer)) != -1) {
					// 从缓冲区（0）位置写入（字节）字节
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}

	public static void unZipFolder(String zipFileString, String outPathString, String szName) throws Exception {
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		while ((zipEntry = inZip.getNextEntry()) != null) {
			if (zipEntry.isDirectory()) {
				//获取部件的文件夹名
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();
			} else {
				File file = new File(outPathString + File.separator + szName);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				// 获取文件的输出流
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// 读取（字节）字节到缓冲区
				while ((len = inZip.read(buffer)) != -1) {
					// 从缓冲区（0）位置写入（字节）字节
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}

	// -----------------------------------------------------------------------

	/**
	 * 返回ZIP中的文件列表（文件和文件夹）
	 *
	 * @param zipFileString  ZIP的名称
	 * @param bContainFolder 是否包含文件夹
	 * @param bContainFile   是否包含文件
	 * @return 文件列表
	 * @throws Exception 异常
	 */
	public static List<File> getFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception {
		List<File> fileList = new ArrayList<File>();
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				// 获取部件的文件夹名
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(szName);
				if (bContainFolder) {
					fileList.add(folder);
				}
			} else {
				File file = new File(szName);
				if (bContainFile) {
					fileList.add(file);
				}
			}
		}
		inZip.close();
		return fileList;
	}

}
