package com.dinson.blingbase.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5 {

    /**
     * md5加密
     */
    fun encode(source: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(source.toByteArray())
        return bytesToHex(bytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}