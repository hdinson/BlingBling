package dinson.customview._global

import android.os.Environment
import com.dinson.blingbase.RxBling

import java.io.File

/**
 * 常量类
 */
object ConstantsUtils {
    val SDCARD_PRIVATE = RxBling.context.externalCacheDirs[0].path + File.separator
    val SDCARD_PRIVATE_CACHE = RxBling.context.getExternalFilesDirs("cache")[0].path + File.separator
    val SDCARD_PRIVATE_IMAGE = RxBling.context.getExternalFilesDirs("Panorama")[0].path + File.separator

    val SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + File.separator

    const val LOGCAT_TAG = "│ --> "
    const val WAN_ANDROID_DOMAIN = "www.wanandroid.com"

    const val APP_FONT_PATH = "fonts/AppleFontWithoutChinese.ttf"
    const val ICON_FONT_PATH = "iconfont/iconfont.ttf"
}
