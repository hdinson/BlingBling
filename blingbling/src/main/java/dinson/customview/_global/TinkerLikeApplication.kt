package dinson.customview._global

import android.app.Application
import android.content.Context
import android.content.Intent
import com.tencent.tinker.anno.DefaultLifeCycle
import com.tencent.tinker.loader.app.DefaultApplicationLike
import com.tencent.tinker.loader.shareutil.ShareConstants

/**
 * Tinker带有生命周期的application
 */
@DefaultLifeCycle(
    application = ".BLingTinkerApplication",
    flags = ShareConstants.TINKER_ENABLE_ALL,
    loadVerifyFlag = false
)
class TinkerLikeApplication(application: Application?, tinkerFlags: Int,
                            tinkerLoadVerifyFlag: Boolean,
                            applicationStartElapsedTime: Long,
                            applicationStartMillisTime: Long,
                            tinkerResultIntent: Intent?)
    : DefaultApplicationLike(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent) {

    override fun onBaseContextAttached(base: Context?) {
        super.onBaseContextAttached(base)
        TinkerManager.installTinker(this)
    }
}