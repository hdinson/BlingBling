package com.dinson.blingbase.utils

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import androidx.collection.SimpleArrayMap

/**
 * 字体工具（优化向）
 */
object TypefaceUtil {
    private val TYPEFACE_CACHE = SimpleArrayMap<String, Typeface>()

    @JvmStatic
    fun getFontFromAssets(context: Context, path: String): Typeface? {
        synchronized(TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(path)) {
                try {
                    val t = Typeface.createFromAsset(context.assets, path)
                    TYPEFACE_CACHE.put(path, t)
                } catch (e: Exception) {
                    Log.e("TypefaceUtils", "Could not get typeface '$path' because ${e.message}")
                    return null
                }
            }
            return TYPEFACE_CACHE.get(path)
        }
    }

}
