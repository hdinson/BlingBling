package com.dinson.blingbase.network

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.dinson.blingbase.kotlin.logi
import com.dinson.blingbase.kotlin.logv
import com.dinson.blingbase.utils.NetworkUtils
import org.greenrobot.eventbus.EventBus

/**
 * NetworkCallback实现
 */
class NetworkCallbackImpl(var mNetType: NetworkType) : ConnectivityManager.NetworkCallback() {
    /**
     * 网络可用的回调
     */
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        val networkType = NetworkUtils.getNetworkType()
        "网络已连接: ${networkType.name}".logi()
        EventBus.getDefault().post(networkType)
    }

    /**
     * 网络丢失的回调
     */
    override fun onLost(network: Network) {
        super.onLost(network)
        "onLost: 网络已断开".logi()
        EventBus.getDefault().post(NetworkType.NETWORK_OFF)
    }


    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    mNetType = NetworkType.NETWORK_WIFI
                    EventBus.getDefault().post(NetworkType.NETWORK_WIFI)
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    mNetType = NetworkUtils.getNetworkType()
                    EventBus.getDefault().post(mNetType)
                }
                else -> {
                    "onCapabilitiesChanged: 其他网络".logv()
                }
            }
        }
    }

    /**
     * 当建立网络连接时，回调连接的属性
     */
    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        "onLinkPropertiesChanged".logv()
    }

    /**
     * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
     */
    override fun onUnavailable() {
        super.onUnavailable()
        "onUnavailable".logv()
    }
}