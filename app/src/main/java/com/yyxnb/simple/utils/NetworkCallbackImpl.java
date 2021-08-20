package com.yyxnb.simple.utils;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;

import com.yyxnb.what.log.LogUtils;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/10
 * 描    述：监听网络变化
 * ================================================
 */
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    /**
     * 网络可用的回调连接成功
     *
     * @param network
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        LogUtils.i("网络已链接");
    }

    /**
     * 网络不可用时调用和onAvailable成对出现
     *
     * @param network
     */
    @Override
    public void onLost(Network network) {
        super.onLost(network);
        LogUtils.i("网络已断开");
    }

    /**
     * 网络功能更改 满足需求时调用
     *
     * @param network
     * @param networkCapabilities
     */
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        LogUtils.i(String.format("onCapabilitiesChanged %s %s", network, networkCapabilities));
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                LogUtils.i("wifi已经连接");
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                LogUtils.i("数据流量已经连接");
            } else {
                LogUtils.i("其他网络");
            }
        }
    }

    /**
     * 网络连接属性修改时调用
     *
     * @param network
     * @param linkProperties
     */
    @Override
    public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        LogUtils.i(String.format("onLinkPropertiesChanged %s %s", network, linkProperties));
    }

    /**
     * 网络缺失network时调用
     */
    @Override
    public void onUnavailable() {
        super.onUnavailable();
        LogUtils.i("onUnavailable");
    }

}