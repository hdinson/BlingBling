package com.dinson.blingbase.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipboardUtils {
    /**
     * 获取剪贴板上的所有文本
     */
    fun getClipboardStrList(context: Context): ArrayList<String>? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 获取剪贴板的剪贴数据集
        val clipData = clipboard.primaryClip
        if (clipData == null || clipData.itemCount == 0) return null
        val arr = java.util.ArrayList<String>()
        for (i in 0 until clipData.itemCount) {
            arr.add(clipData.getItemAt(i).text.toString())
        }
        return arr
    }

    /**
     * 获取剪切板上最后一条文本
     */
    fun getClipboardStrFirst(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 获取剪贴板的剪贴数据集
        val clipData = clipboard.primaryClip
        if (clipData == null || clipData.itemCount == 0) return null
        return clipData.getItemAt(0).text?.toString()
    }

    /**
     * 拷贝到剪切板
     */
    fun copyToClipBoard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("words", text)
        clipboard.setPrimaryClip(clip)
    }
}