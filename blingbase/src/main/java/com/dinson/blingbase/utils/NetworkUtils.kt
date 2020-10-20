package com.dinson.blingbase.utils

import android.content.Context
import android.telephony.TelephonyManager
import com.dinson.blingbase.RxBling
import com.dinson.blingbase.network.NetworkType


object NetworkUtils {

    fun getNetworkType(): NetworkType {
        val tm = RxBling.getApplicationContext()
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (tm.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.NETWORK_3G
            TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G
            TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NETWORK_5G
            else -> NetworkType.NETWORK_UNKNOWN
        }
    }

}