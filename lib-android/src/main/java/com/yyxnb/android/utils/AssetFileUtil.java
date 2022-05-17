package com.yyxnb.android.utils;

import android.content.Context;

import com.yyxnb.android.log.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Asset文件处理
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/24
 */
public class AssetFileUtil {

	public static boolean copyAssetFile(Context context, String originAssetFileName, String destFileDirectory, String destFileName) {
		long startTime = System.currentTimeMillis();
		InputStream is = null;
		BufferedOutputStream bos = null;
		try {
			is = context.getAssets().open(originAssetFileName);

			File destPathFile = new File(destFileDirectory);
			if (!destPathFile.exists()) {
				destPathFile.mkdirs();
			}

			File destFile = new File(destFileDirectory + File.separator + destFileName);
			if (!destFile.exists()) {
				destFile.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(destFile);
			bos = new BufferedOutputStream(fos);

			byte[] buffer = new byte[256];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				bos.write(buffer, 0, length);
			}
			bos.flush();
			return true;
		} catch (Exception e) {
			LogUtil.e(e.getMessage());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					LogUtil.e(e.getMessage());
				}
			}
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					LogUtil.e(e.getMessage());
				}
			}
		}
		return false;
	}

}
