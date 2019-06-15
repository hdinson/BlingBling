package dinson.customview._global

import android.os.Environment

import java.io.File

/**
 * 常量类
 */
object ConstantsUtils {
    val SDCARD_PRIVATE = GlobalApplication.getContext().externalCacheDirs[0].path + File.separator
    val SDCARD_PRIVATE_CACHE = GlobalApplication.getContext().getExternalFilesDirs("cache")[0].path + File.separator
    val SDCARD_PRIVATE_IMAGE = GlobalApplication.getContext().getExternalFilesDirs("Panorama")[0].path + File.separator

    val SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + File.separator

    const val LOGCAT_TAG = "│ --> "
    const val WAN_ANDROID_DOMAIN = "www.wanandroid.com"
}
