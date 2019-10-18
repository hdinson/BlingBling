package dinson.customview.utils

import android.content.Context
import android.graphics.Typeface
import androidx.collection.SimpleArrayMap

/**
 * 字体工具（优化向）
 */
object TypefaceUtils {
    private val TYPEFACE_CACHE = SimpleArrayMap<String, Typeface>()

    /**
     * 获取兰亭字体
     */
    fun getAppleFont(context: Context): Typeface? {
        return get(context, "fonts/AppleFontWithoutChinese.ttf")
    }

    /**
     * 获取IconFont字体
     */
    fun getIconFont(context: Context): Typeface? {
        return get(context, "iconfont/iconfont.ttf")
    }

    private operator fun get(context: Context, name: String): Typeface? {
        synchronized(TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {

                try {
                    val t = Typeface.createFromAsset(context.assets, name)
                    TYPEFACE_CACHE.put(name, t)
                } catch (e: Exception) {
                    LogUtils.e("Could not get typeface '" + name + "' because " + e.message)
                    return null
                }

            }
            return TYPEFACE_CACHE.get(name)
        }
    }

}
