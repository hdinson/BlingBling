package com.dinson.blingbase.utils

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import androidx.collection.SimpleArrayMap

/**
 * 字体工具（优化向）
 */
object TypefaceUtils {
    private val TYPEFACE_CACHE = SimpleArrayMap<String, Typeface>()

    private operator fun get(context: Context, name: String): Typeface? {
        synchronized(TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {
                try {
                    val t = Typeface.createFromAsset(context.assets, name)
                    TYPEFACE_CACHE.put(name, t)
                } catch (e: Exception) {
                    Log.e("TypefaceUtils", "Could not get typeface '" + name + "' because " + e.message)
                    return null
                }

            }
            return TYPEFACE_CACHE.get(name)
        }
    }
}
