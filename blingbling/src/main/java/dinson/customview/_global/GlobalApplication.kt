package dinson.customview._global

import android.app.Application
import com.dinson.blingbase.RxBling
import com.dinson.blingbase.crash.ActivityCrash
import com.dinson.blingbase.crash.CrashProfile
import com.dinson.blingbase.crash.CrashTool
import com.dinson.blingbase.rxcache.RxCache
import com.dinson.blingbase.rxcache.diskconverter.GsonDiskConverter
import com.huawei.hms.mlsdk.common.MLApplication
import dinson.customview.BuildConfig
import dinson.customview.activity.SplashActivity
import dinson.customview.kotlin.logi
import java.io.File


class  GlobalApplication : Application() {

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

        MLApplication.getInstance().apiKey = BuildConfig.HUAWEI_API_KEY

        RxCache.initializeDefault(
            RxCache.Builder()
                .appVersion(1) //当版本号改变,缓存路径下存储的所有数据都会被清除掉
                .diskDir(File(cacheDir.path + File.separator.toString() + "data-cache"))
                .diskConverter(GsonDiskConverter()) //支持Serializable、Json(GsonDiskConverter)
                .memoryMax(2 * 1024 * 1024)
                .diskMax(20 * 1024 * 1024.toLong())
                .showLog(BuildConfig.DEBUG)
                .build()
        )
    }
}