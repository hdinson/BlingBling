package com.dinson.blingbase.utils

import android.content.Context
import android.content.pm.ApplicationInfo


@Suppress("unused")
object BlingBaseApplication {

    private var mContext: Context? = null
    private var mIsDebug: Boolean? = null

    fun init(context: Context) {
        mContext = context
        mIsDebug = context.applicationInfo != null && (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    fun getContext(): Context {
        if (mContext == null) {
            throw RuntimeException("should call BlingBaseApplication.init(context) at application !")
        }
        return mContext!!
    }

    fun isDebug() = mIsDebug ?: false


}