package com.yyxnb.what.file;

import android.os.FileObserver;
import android.text.TextUtils;
import android.util.Log;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/05/07
 * 描    述：自身包下的文件监听
 * <p>
 * LocalFileObserver observer = new LocalFileObserver(path);
 * observer.startWatching();//启动文件监听
 * observer.stopWatching();//停止文件监听
 * ================================================
 */
public class LocalFileObserver extends FileObserver {
    private static final String TAG = "LocalFileObserver";
    private final String parentPath;

    /**
     * 如此构造的对象监听所有FileObserver可以监听的事件，path为需监听的文件路径
     *
     * @param path 文件路径
     */
    public LocalFileObserver(String path) {
        super(path);
        this.parentPath = path;
    }

    /**
     * 这样构造的对象只监听mask对应的事件
     *
     * @param path
     * @param mask
     */
    public LocalFileObserver(String path, int mask) {
        super(path, mask);
        this.parentPath = path;
    }

    /**
     * 需要实现的方法（当触发监听事件时系统会自动回调该方法）
     *
     * @param event
     * @param path
     */
    @Override
    public void onEvent(int event, String path) {
        String accessPath;
        //path为null表示自身目录
        if (TextUtils.isEmpty(path)) {
            accessPath = parentPath;
        } else {
            accessPath = parentPath + "/" + path;
        }
        //文件被访问
        if (event == FileObserver.ACCESS) {
            Log.i(TAG, "文件被访问:" + accessPath);
        }
        //文件被修改
        else if (event == FileObserver.MODIFY) {
            Log.i(TAG, "文件被修改:" + accessPath);
        }
        //文件属性被修改
        else if (event == FileObserver.ATTRIB) {
            Log.i(TAG, "文件属性被修改:" + accessPath);
        }
        //可写文件被关闭
        else if (event == FileObserver.CLOSE_WRITE) {
            Log.i(TAG, "可写文件被关闭:" + accessPath);
        }
        //不可写文件被关闭
        else if (event == FileObserver.CLOSE_NOWRITE) {
            Log.i(TAG, "不可写文件被关闭:" + accessPath);
        }
        //文件被打开
        else if (event == FileObserver.OPEN) {
            Log.i(TAG, "文件被打开:" + accessPath);
        }
        //文件被移走
        else if (event == FileObserver.MOVED_FROM) {
            Log.i(TAG, "文件被移走:" + accessPath);
        }
        //文件被移来
        else if (event == FileObserver.MOVED_TO) {
            Log.i(TAG, "文件被移来:" + accessPath);
        }
        //创建新文件
        else if (event == FileObserver.CREATE) {
            Log.i(TAG, "创建新文件:" + accessPath);
        }
        //文件被删除
        else if (event == FileObserver.DELETE) {
            Log.i(TAG, "文件被删除:" + accessPath);
        }
        //自删除，即一个可执行文件在执行时删除自己
        else if (event == FileObserver.DELETE_SELF) {
            Log.i(TAG, "自删除，即一个可执行文件在执行时删除自己:" + accessPath);
        }
        //自移动，即一个可执行文件在执行时移动自己
        else if (event == FileObserver.MOVE_SELF) {
            Log.i(TAG, "自移动，即一个可执行文件在执行时移动自己:" + accessPath);
        }
        //包括上面的所有事件
        else if (event == FileObserver.ALL_EVENTS) {
            Log.i(TAG, "包括上面的所有事件:" + accessPath);
        }
    }
}
