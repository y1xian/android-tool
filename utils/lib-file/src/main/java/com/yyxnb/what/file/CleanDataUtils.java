package com.yyxnb.what.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/17
 * 描    述：清除数据
 * ================================================
 */
public class CleanDataUtils {

    /**
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */

    private CleanDataUtils() {
    }


    /**
     * 清除所有缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            //TODO 有网页清理时注意排错，是否存在/data/data/应用package目录下找不到database文件夹的问题
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        }
    }

    /**
     * 删除某个文件
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        if (dir != null) {
            return dir.delete();
        } else {
            return false;
        }
    }


}