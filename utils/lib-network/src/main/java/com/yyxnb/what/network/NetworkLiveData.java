package com.yyxnb.what.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.yyxnb.what.application.ApplicationUtils;
import com.yyxnb.what.log.LogUtils;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/10
 * 描    述：LiveData + NetworkCallback实现网络监听
 * ================================================
 */
public class NetworkLiveData extends LiveData<NetworkState> {

    private final ConnectivityManager.NetworkCallback networkCallback;
    private final NetworkRequest request;
    private final ConnectivityManager manager;
    private static volatile NetworkLiveData mInstance = null;

    private NetworkLiveData() {
        networkCallback = new NetworkCallbackImpl();
        request = new NetworkRequest.Builder().build();
        manager = (ConnectivityManager) ApplicationUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetworkLiveData getInstance() {
        if (null == mInstance) {
            synchronized (NetworkLiveData.class) {
                if (null == mInstance) {
                    mInstance = new NetworkLiveData();
                }
            }
        }
        return mInstance;
    }


    @Override
    protected void onActive() {
        super.onActive();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.registerDefaultNetworkCallback(networkCallback);
        } else {
            manager.registerNetworkCallback(request, networkCallback);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        manager.unregisterNetworkCallback(networkCallback);
    }

    /**
     * NetworkCallback实现网络监听
     */
    private static class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

        /**
         * 网络可用的回调连接成功
         *
         * @param network
         */
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            LogUtils.i("网络已链接");
            getInstance().postValue(NetworkState.NETWORK_CONNECT);
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
            getInstance().postValue(NetworkState.NETWORK_NONE);
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
                    getInstance().postValue(NetworkState.NETWORK_WIFI);
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    LogUtils.i("数据流量已经连接");
                    getInstance().postValue(NetworkState.NETWORK_MOBILE);
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
}
