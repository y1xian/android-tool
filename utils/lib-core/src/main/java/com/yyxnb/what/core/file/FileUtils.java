package com.yyxnb.what.core.file;

import com.yyxnb.what.log.LogUtils;

import java.io.File;
import java.io.IOException;


public class FileUtils {

    public static final long KB = 1024;
    public static final long MB = 1024 * KB;
    public static final long GB = 1024 * MB;
    public static final int UNZIP_MAX_FILE_NUMBER = 10000;


    public static String getSuffix(File file) {
        return FileNameUtils.getSuffix(file);
    }

    public static String getSuffix(String fileName) {
        return FileNameUtils.getSuffix(fileName);
    }

    public static String getPath(File file) {
        if (file == null) {
            return null;
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            LogUtils.e("获取路径异常", e);
        }
        return null;
    }


}
