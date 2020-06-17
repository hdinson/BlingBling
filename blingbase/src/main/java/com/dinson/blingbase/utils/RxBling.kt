package com.dinson.blingbase.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import com.dinson.blingbase.crash.CrashProfile
import com.dinson.blingbase.network.NetworkListener


@Suppress("unused")
object RxBling {

    private var mContext: Context? = null
    private var mIsDebug: Boolean? = null

    @JvmStatic
    fun init(context: Context): CrashProfile.Builder {
        mContext = context
        mIsDebug = context.applicationInfo != null && (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        NetworkListener.getInstance().init(context)
        return CrashProfile.Builder.create()
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
}