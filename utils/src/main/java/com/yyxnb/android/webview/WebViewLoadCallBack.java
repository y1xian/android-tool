/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2020. All rights reserved.
 */

package com.yyxnb.android.webview;

/**
 * <BR>
 *
 * @author c00373278
 * @version [v1.0.0.301, 2019/4/29]
 */

public interface WebViewLoadCallBack {

    void onCheckError(String url, ErrorCode errorCode);

    /**
     * <BR>
     *
     * @author c00373278
     * @version [v1.0.0.301, 2019/4/29]
     */

    enum ErrorCode {
        /**
         * HTTP的url，建议使用系统浏览器加载
         */
        HTTP_URL,
        /**
         * 不在白名单的url
         */
        URL_NOT_IN_WHITE_LIST,
        /**
         * 其它类型
         */
        OTHER
    }
}
