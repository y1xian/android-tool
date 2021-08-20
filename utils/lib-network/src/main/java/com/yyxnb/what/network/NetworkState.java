package com.yyxnb.what.network;

public enum NetworkState {

    /**
     * 没有连接网络
     */
    NETWORK_NONE(0, "没有连接网络"),
    /**
     * 网络已链接
     */
    NETWORK_CONNECT(1, "网络已链接"),
    /**
     * wifi已经连接
     */
    NETWORK_WIFI(2, "wifi已经连接"),
    /**
     * 数据流量已经连接
     */
    NETWORK_MOBILE(3, "数据流量已经连接");

    private int code;
    private String msg;

    NetworkState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}