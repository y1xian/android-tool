package com.yyxnb.oh.network;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yyxnb.oh.network.event.NetworkEvent;


public class NetworkCallback extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetworkCallback";
    private static final String NETWORK = "Network";

    /**
     * 网络可用的回调连接成功
     *
     * @param network
     */
    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        Log.i(TAG, "NetworkCallback onAvailable " + Thread.currentThread().getName());
        LiveEventBus.get(NETWORK).post(new NetworkEvent(true));
    }

    /**
     * 网络不可用时调用和onAvailable成对出现
     *
     * @param network
     */
    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Log.e(TAG, "NetworkCallback onLost");
        LiveEventBus.get(NETWORK).post(new NetworkEvent(false));
    }

    /**
     * 在网络连接正常的情况下，丢失数据会有回调 即将断开时
     *
     * @param network
     * @param maxMsToLive
     */
    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        Log.e(TAG, "NetworkCallback onLosing");
        LiveEventBus.get(NETWORK).post(new NetworkEvent(false));
    }

    /**
     * 网络功能更改 满足需求时调用
     *
     * @param network
     * @param networkCapabilities
     */
    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        Log.i(TAG, "AdcNetworkCallback onCapabilitiesChanged");
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
        Log.i(TAG, "AdcNetworkCallback onLinkPropertiesChanged");
    }

    /**
     * 网络缺失network时调用
     */
    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Log.e(TAG, "NetworkCallback onUnavailable");
        LiveEventBus.get(NETWORK).post(new NetworkEvent(false));
    }
}
