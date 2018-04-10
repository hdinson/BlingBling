package dinson.customview._global

import android.content.Context
import com.tencent.tinker.lib.tinker.Tinker
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.tencent.tinker.loader.app.ApplicationLike

/**
 * Tinker的封装
 */
object TinkerManager {

    private var isInstalled = false
    private var mAppLike: ApplicationLike? = null

    fun installTinker(applicationLike: ApplicationLike) {
        if (isInstalled) return
        TinkerInstaller.install(applicationLike)
        mAppLike = applicationLike
        isInstalled = true
    }

    fun loadPatch(path: String) {
        if (Tinker.isTinkerInstalled()) {
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                path)
        }
    }

    fun getApplicationContext(): Context? {
        return mAppLike?.application?.applicationContext
    }

}