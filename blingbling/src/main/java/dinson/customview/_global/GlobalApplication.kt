package dinson.customview._global

import android.app.Application
import com.dinson.blingbase.crash.ActivityCrash
import com.dinson.blingbase.crash.CrashProfile
import com.dinson.blingbase.crash.CrashTool
import com.dinson.blingbase.kotlin.logi
import com.dinson.blingbase.kotlin.toasty
import com.dinson.blingbase.RxBling
import dinson.customview.BuildConfig
import dinson.customview.activity.SplashActivity

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RxBling.init(this)
            //.initNetWorkListener()
            .initCrashModule()
            .backgroundMode(CrashProfile.BACKGROUND_MODE_SILENT) //default: TCrashProfile.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .showErrorDetails(true) //default: true
            .showRestartButton(true) //default: true
            .logErrorOnRestart(true) //default: true
            .trackActivities(true) //default: false
            .minTimeBetweenCrashesMs(2000) //default: 3000
            .restartActivity(SplashActivity::class.java) //default: null (your app's launch activity)
            .errorActivity(ActivityCrash::class.java) //default: null (default error activity)
            .eventListener(object : CrashTool.EventListener {
                override fun onRestartAppFromErrorActivity() {
                    "onRestartAppFromErrorActivity".toasty().logi()
                }

                override fun onCloseAppFromErrorActivity() {
                    "onCloseAppFromErrorActivity".toasty().logi()
                }

                override fun onLaunchErrorActivity() {
                    ("onLaunchErrorActivity").toasty().logi()
                }

            }) //default: null
            .apply()
    }

    companion object {
        @JvmField
        val IS_DEBUG = BuildConfig.DEBUG

    }
}