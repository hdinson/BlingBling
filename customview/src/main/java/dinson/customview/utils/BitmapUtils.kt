package dinson.customview.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView


/**
 * Bitmap工具类（从view中创建视图）
 */
object BitmapUtils {
    private val sCanvas = Canvas()

    /**
     * 从view中创建视图(当视图过大时返回null)
     */
    fun createBitmapFromView(view: View): Bitmap? {
        if (view is ImageView) {
            val drawable = view.drawable
            if (drawable != null && drawable is BitmapDrawable)
                return drawable.bitmap
        }
        view.clearFocus()
        val bitmap = createBitmapSafely(view.width, view.height,
            Bitmap.Config.ARGB_8888, 1)
        if (bitmap != null) {
            synchronized(sCanvas) {
                val canvas = sCanvas
                canvas.setBitmap(bitmap)
                view.draw(canvas)
                canvas.setBitmap(null)
            }
        }
        return bitmap
    }

    /**
     * 内存溢出检测
     */
    private fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
        try {
            return Bitmap.createBitmap(width, height, config)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(width, height, config, retryCount - 1)
            }
            return null
        }
    }
}
