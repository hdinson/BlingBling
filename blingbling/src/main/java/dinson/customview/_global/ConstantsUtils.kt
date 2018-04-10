package dinson.customview._global

import android.os.Environment

import java.io.File

import dinson.customview.utils.UIUtils

/**
 * 常量类
 */
object ConstantsUtils {
    var SDCARD_PRIVATE = UIUtils.getContext().externalCacheDirs[0].path
    var SDCARD_PRIVATE_CACHE = UIUtils.getContext().getExternalFilesDirs("cache")[0].path + File.separator
    var SDCARD_PRIVATE_IMAGE = UIUtils.getContext().getExternalFilesDirs("Panorama")[0].path + File.separator


    var SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + File.separator

    var LOGCAT_TAG = "│ --> "
    var PACKAGE_NAME = "dinson.customview"
    var WANANDROID_DOMAIN = "www.wanandroid.com"
}
