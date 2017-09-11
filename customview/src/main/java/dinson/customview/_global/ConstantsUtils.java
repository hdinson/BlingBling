package dinson.customview._global;

import android.os.Environment;

import java.io.File;

import dinson.customview.utils.UIUtils;

/**
 * @author Dinson - 2017/9/7
 */
public class ConstantsUtils {
    public static String SDCARD_PRIVATE = UIUtils.getContext().getExternalCacheDirs()[0].getPath();
    public static String SDCARD_PRIVATE_CACHE = UIUtils.getContext().getExternalFilesDirs("cache")[0].getPath() + File.separator;
    public static String SDCARD_PRIVATE_IMAGE = UIUtils.getContext().getExternalFilesDirs("Panorama")[0].getPath() + File.separator;


    public static String SDCARD_ROOT = Environment.getExternalStorageDirectory() + File.separator;


}
