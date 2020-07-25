package com.dinson.blingbase.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import java.io.OutputStream


/**
 * Bitmap工具类（从view中创建视图）
 */
@Suppress("unused")
object BitmapUtils {
    private val sCanvas = Canvas()

    /**
     * 从view中创建视图
     * @return 当视图过大时返回 null
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
     * 保存图片到相册
     */
    private fun addPictureToAlbum(context: Context, bitmap: Bitmap): Boolean {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, DateUtils.currentTimeMillis13().toString())
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val outputStream: OutputStream?
        return try {
            //获取刚插入的数据的Uri对应的输出流
            outputStream = context.contentResolver.openOutputStream(uri!!)
            //将bitmap图片保存到Uri对应的数据节点中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            //图片会保存到sd卡的pcitures目录下1487231905572.jpg
            outputStream?.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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
