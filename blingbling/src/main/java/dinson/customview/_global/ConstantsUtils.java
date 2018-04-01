package dinson.customview._global;

import android.os.Environment;

import java.io.File;

import dinson.customview.utils.UIUtils;

/**
 * 常量类
 */
public class ConstantsUtils {
    public static String SDCARD_PRIVATE = UIUtils.getContext().getExternalCacheDirs()[0].getPath();
    public static String SDCARD_PRIVATE_CACHE = UIUtils.getContext().getExternalFilesDirs("cache")[0].getPath() + File.separator;
    public static String SDCARD_PRIVATE_IMAGE = UIUtils.getContext().getExternalFilesDirs("Panorama")[0].getPath() + File.separator;


    public static String SDCARD_ROOT = Environment.getExternalStorageDirectory() + File.separator;

    public static String LOGCAT_TAG = "│ --> ";
    public static String PACKAGE_NAME = "dinson.customview";
    public static String WANANDROID_DOMAIN = "www.wanandroid.com";
}
