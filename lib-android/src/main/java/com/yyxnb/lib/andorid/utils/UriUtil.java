package com.yyxnb.lib.andorid.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.yyxnb.lib.andorid.log.LogUtil;
import com.yyxnb.lib.java.io.FileUtil;
import com.yyxnb.lib.java.io.IoUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * UriUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/8
 */
public class UriUtil {

	/**
	 * 通过uri获取文件地址
	 * Android 10 以上适配
	 *
	 * @param context 上下文
	 * @param uri     uri
	 * @return 文件地址
	 */
	public static String uriToFileApiQ(Context context, Uri uri) {
		File file = null;
		//android10以上转换
		if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
			file = new File(uri.getPath());
		} else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
			//把文件复制到沙盒目录
			ContentResolver contentResolver = context.getContentResolver();
			Cursor cursor = contentResolver.query(uri, null, null, null, null);
			if (cursor.moveToFirst()) {
				String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				try {
					InputStream is = contentResolver.openInputStream(uri);
					File cache = new File(context.getExternalCacheDir(), displayName);
					FileOutputStream fos = new FileOutputStream(cache);
					FileUtil.write(is, cache);
					file = cache;
					IoUtil.close(fos, is);
				} catch (Exception e) {
					LogUtil.e("复制文件异常", e);
				}
			}
		}
		return FileUtil.getCanonicalPath(file);
	}
}
