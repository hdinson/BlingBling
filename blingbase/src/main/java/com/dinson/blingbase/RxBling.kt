package com.dinson.blingbase

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.dinson.blingbase.crash.CrashProfile
import com.dinson.blingbase.network.NetworkType
import com.dinson.blingbase.network.NetworkCallbackImpl


@Suppress("unused")
object RxBling {

    private var mContext: Context? = null
    private var mIsDebug: Boolean? = null

    private var mNetType = NetworkType.NETWORK_UNKNOWN

    @JvmStatic
    fun init(context: Context): RxBling {
        mContext = context
        mIsDebug = context.applicationInfo != null && (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return this
    }

    @JvmStatic
    val context: Context
        get() {
            if (mContext == null) {
                throw RuntimeException("should call BlingBaseApplication.init(context) at application !")
            }
            return mContext!!
        }

    fun isDebug() = mIsDebug ?: false

    /**
     * 设置网络监听
     */
    fun initNetWorkListener(): RxBling {
        //添加网络监听
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr.registerNetworkCallback(NetworkRequest.Builder().build(), NetworkCallbackImpl(mNetType))
        return this
    }

    /**
     * 设置崩溃日志
     */
    fun initCrashModule(): CrashProfile.Builder {
        return CrashProfile.Builder.create()
    }
}