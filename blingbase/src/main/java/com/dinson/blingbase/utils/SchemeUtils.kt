package com.dinson.blingbase.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.IntDef
import com.dinson.blingbase.kotlin.hasInstalled
import com.dinson.blingbase.utils.SchemeUtils.Result.Companion.NONE
import com.dinson.blingbase.utils.SchemeUtils.Result.Companion.SUCCESS
import java.net.URL

object SchemeUtils {

    @IntDef(Result.SUCCESS, Result.NO_INSTALL, Result.NONE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Result {
        companion object {
            const val SUCCESS = 0x00000001
            const val NO_INSTALL = 0x00000002
            const val NONE = 0x00000004
        }
    }

    fun openTaoBaoApp(context: Context, url: String): Pair<Intent?, Int> {
        if (url.isEmpty()) return null to NONE
        return if (context.hasInstalled("com.taobao.taobao")) {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val uri = Uri.parse(url)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent to SUCCESS
        } else null to Result.NO_INSTALL
    }
}