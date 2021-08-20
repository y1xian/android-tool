package com.yyxnb.what.log;

import android.text.TextUtils;

import com.yyxnb.what.application.ApplicationUtils;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/14
 * 描    述：LogConfig
 * ================================================
 */
public class LogConfig {

    /**
     * 打印堆栈信息
     */
    private boolean showThreadInfo = true;
    /**
     * 是否打印日志
     */
    private boolean showLog = ApplicationUtils.isDebug();
    /**
     * tag标识
     */
    private String tag = ">----Oh---->";
    /**
     * Log监听、可在其操作日志存储
     */
    private ILog.ILogPrintListener logPrintListener = null;

    public LogConfig setLogWriteListener(ILog.ILogPrintListener logPrintListener) {
        this.logPrintListener = logPrintListener;
        return this;
    }


    public LogConfig setTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            this.tag = tag;
        }
        return this;
    }

    public LogConfig setShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }

    public LogConfig setShowLog(boolean showLog) {
        this.showLog = showLog;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public ILog.ILogPrintListener getLogPrintListener() {
        return logPrintListener;
    }
}