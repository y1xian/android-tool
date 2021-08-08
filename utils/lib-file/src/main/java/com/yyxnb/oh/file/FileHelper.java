package com.yyxnb.oh.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileHelper {

    public static final long KB = 1024;

    public static final long MB = 1024 * KB;

    public static final long GB = 1024 * MB;

    public static final long TB = 1024 * GB;

      /*
        Context.getPackageName();           // 用于获取APP的所在包目录
        Context.getPackageCodePath();       //来获得当前应用程序对应的apk文件的路径
        Context.getPackageResourcePath();   // 获取该程序的安装包路径
        Context.getDatabasePath();          //返回通过Context.openOrCreateDatabase创建的数据库文件


        // 一般存放临时缓存数据
        Context.getExternalCacheDir();              // /data/data/package/cache/      随着应用的卸载存储的文件被删除
        Context.getCacheDir();                      // /data/user/0/package/cache/    随着应用的卸载存储的文件被删除,目录存在app的内部存储上，无法找到
        // 一般放一些长时间保存的数据
        Context.getExternalFilesDir(null);          // /storage/emulated/0/Android/data/package/files/  随着应用的卸载存储的文件被删除
        Context.getFilesDir();                      // /data/user/0/package/files/  随着应用的卸载存储的文件被删除,目录存在app的内部存储上，无法找到

        // Environment类去获取外部存储目录 Android Q android:requestLegacyExternalStorage="true"
        Environment.getDataDirectory().getPath(); 　　　　　　   // /storage/emulated/0/  获得根目录/data
        Environment.getDownloadCacheDirectory().getPath();     // /data/cache/  获得下载缓存目录 /cache
        Environment.getExternalStorageDirectory().getPath();   // /storage/emulated/0/  获得SD卡目录 /mnt/sdcard ，跟应用的是否卸载无关。
        Environment.getRootDirectory().getPath();   　　　　    // 获得系统主目录 /system
     */

    public static File getExternalStorageDirectory() {
        return new File(Environment.getExternalStorageDirectory().getPath());
    }

    public static File getExternalFilesDir(Context context) {
        return context.getExternalFilesDir(null);
    }

    public static File getExternalFilesDir(Context context, String dir) {
        return context.getExternalFilesDir(dir);
    }

}
