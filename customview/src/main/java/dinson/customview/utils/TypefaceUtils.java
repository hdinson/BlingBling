package dinson.customview.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

/**
 * 字体工具（优化向）
 */
public class TypefaceUtils {
    private static final SimpleArrayMap<String, Typeface> TYPEFACE_CACHE = new SimpleArrayMap<>();

    /**
     * 获取兰亭字体
     */
    public static Typeface getAppleFont(Context context) {
        return get(context, "fonts/AppleFontWithoutChinese.ttf");
    }

    /**
     * 获取IconFont字体
     */
    public static Typeface getIconFont(Context context) {
        return get(context, "iconfont/iconfont.ttf");
    }

    private static Typeface get(Context context, String name) {
        synchronized (TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {

                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), name);
                    TYPEFACE_CACHE.put(name, t);
                } catch (Exception e) {
                    LogUtils.e("Could not get typeface '" + name + "' because " + e.getMessage());
                    return null;
                }
            }
            return TYPEFACE_CACHE.get(name);
        }
    }

}
