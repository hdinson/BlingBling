package dinson.customview._global;

import java.io.File;

import dinson.customview.utils.UIUtils;

/**
 * @author Dinson - 2017/9/7
 */
public class ConstantsUtils {
    public static String SDCARD_PRIVATE = UIUtils.getContext().getExternalCacheDirs()[0].getPath();
    public static String SDCARD_PRIVATE_IMAGE = SDCARD_PRIVATE + File.separator + "Panorama"+File.separator;
}
