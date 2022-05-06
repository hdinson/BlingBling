package com.dinson.blingbase.utils

import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable

/**
 *  NFC相关的工具类
 */
object NfcUtils {

    /**
     * 创建快速启动的app
     */
    fun writeApplicationRecord(tag: Tag, packageName: String) =
        write(tag, NdefMessage(arrayOf(NdefRecord.createApplicationRecord(packageName))))


    /**
     * 创建快速启动的app
     */
    fun writeUri(tag: Tag, uriStr: String) =
        write(tag, NdefMessage(arrayOf(NdefRecord.createUri(Uri.parse(uriStr)))))


    /**
     * 创建快速启动的app
     */
    fun writeUri(tag: Tag, uri: Uri) = write(tag, NdefMessage(arrayOf(NdefRecord.createUri(uri))))


    /**
     * 往标签写NdefMessage
     */
    private fun write(tag: Tag, ndefMessage: NdefMessage): Pair<Boolean, String> {
        //转换成字节获得大小
        val size = ndefMessage.toByteArray().size
        try {
            //2.判断NFC标签的数据类型（通过Ndef.get方法）
            val ndef = Ndef.get(tag)
            //判断是否为NDEF标签
            if (ndef == null) {
                //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
                val format = NdefFormatable.get(tag) ?: return false to "该标签非NDEF标签"
                //判断是否获得了NdefFormatAble对象，有一些标签是只读的或者不允许格式化的
                format.connect()
                //格式化并将信息写入标签
                format.format(ndefMessage)
                return true to "写入成功"
            }
            //判断是否支持可写
            if (!ndef.isWritable) return false to "该标签不支持读写"
            //判断标签的容量是否够用
            if (ndef.maxSize < size) return false to "该标签容量不足"
            //3.写入数据
            ndef.connect()
            ndef.writeNdefMessage(ndefMessage)
            return true to "写入成功"
        } catch (e: Exception) {
            e.printStackTrace()
            return true to "发生异常"
        }
    }
}