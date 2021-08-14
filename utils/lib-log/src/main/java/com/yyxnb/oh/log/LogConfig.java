package com.yyxnb.oh.log;

import android.text.TextUtils;

import com.yyxnb.oh.application.ApplicationUtils;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/14
 * 描    述：LogConfig
 * ================================================
 */
public class LogConfig {

    private boolean showThreadInfo = true;
    private boolean debug = ApplicationUtils.isDebug();
    private String tag = ">----oh---->";
    private boolean write = false;


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

    public LogConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public LogConfig setWriteLocal(boolean write) {
        this.write = write;
        return this;
    }

    public boolean isWriteLocal() {
        return write;
    }

    public String getTag() {
        return tag;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }
}