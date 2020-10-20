package com.dinson.blingbase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.dinson.blingbase.crash.CrashProfile
import com.dinson.blingbase.network.NetworkCallbackImpl
import com.dinson.blingbase.network.NetworkType
import com.tencent.mmkv.MMKV

@Suppress("unused")
object RxBling {

    private var mContext: Context? = null
    private var mNetType = NetworkType.NETWORK_UNKNOWN

    @JvmStatic
    fun init(context: Context): RxBling {
        mContext = context
        MMKV.initialize(context)
        return this
    }

    @JvmStatic
    fun getApplicationContext(): Context {
        if (mContext == null) {
            throw RuntimeException("should call RxBling.init(context) at application !")
        }
        return mContext!!
    }

    /**
     * 设置网络监听
     */
    fun initNetWorkListener(): RxBling {
        //添加网络监听
        val connMgr =
            getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            NetworkCallbackImpl(mNetType)
        )
        return this
    }

    /**
     * 设置崩溃日志
     */
    fun initCrashModule(): CrashProfile.Builder {
        return CrashProfile.Builder.create().enabled(false).enabled(false)
    }
}