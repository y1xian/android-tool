package com.yyxnb.what.download;

import com.yyxnb.what.okhttp.AbsOkHttp;

public class DownloadOkHttp extends AbsOkHttp {

    private static volatile DownloadOkHttp mInstance = null;

    private DownloadOkHttp() {
    }

    public static DownloadOkHttp getInstance() {
        if (null == mInstance) {
            synchronized (DownloadOkHttp.class) {
                if (null == mInstance) {
                    mInstance = new DownloadOkHttp();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected String baseUrl() {
        return "";
    }



}
