package dinson.customview._global

import android.app.Application
import com.dinson.blingbase.crash.ActivityCrash
import com.dinson.blingbase.crash.CrashProfile
import com.dinson.blingbase.crash.CrashTool
import dinson.customview.kotlin.logi
import com.dinson.blingbase.RxBling
import com.huawei.hms.mlsdk.common.MLApplication
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
                    logi { "onRestartAppFromErrorActivity" }
                }

                override fun onCloseAppFromErrorActivity() {
                    logi { "onCloseAppFromErrorActivity" }
                }

                override fun onLaunchErrorActivity() {
                    logi { "onLaunchErrorActivity" }
                }

            }) //default: null
            .apply()

        logi { "application init.." }
        MLApplication.getInstance().apiKey = BuildConfig.HUAWEI_API_KEY
    }
}