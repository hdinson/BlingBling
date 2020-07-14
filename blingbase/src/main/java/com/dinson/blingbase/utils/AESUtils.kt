package com.dinson.blingbase.utils

import java.io.*
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密工具类
 */
@Suppress("unused")
object AESUtils {

    private const val HEX = "0123456789ABCDEF"
    private const val keyLength = 16
    private const val defaultV = "0"

    /**
     * 加密
     *
     * @param key 密钥
     * @param src 加密文本
     */
    @Throws(Exception::class)
    fun encrypt(key: String, src: String): String {
        // /src = Base64.encodeToString(src.getBytes(), Base64.DEFAULT);
        val rawKey = toMakeKey(key)  // key.getBytes();
        val result = encrypt(rawKey, src.toByteArray(charset("utf-8")))
        // result = Base64.encode(result, Base64.DEFAULT);
        return toHex(result)
    }

    /**
     * 加密
     *
     * @param key 密钥
     * @param src 加密文本
     * @return
     */
    @Throws(Exception::class)
    fun encrypt2Java(key: String, src: String): String {
        // /src = Base64.encodeToString(src.getBytes(), Base64.DEFAULT);
        val rawKey = toMakeKey(key) // key.getBytes();
        val result = encrypt2Java(rawKey, src.toByteArray(charset("utf-8")))
        // result = Base64.encode(result, Base64.DEFAULT);
        return toHex(result)
    }

    /**
     * 16进制转2进制
     *
     * @param hex 16进制流
     * @return 2进制流
     */
    private fun hexStr2Byte(hex: String): ByteArray {
        val bf = ByteBuffer.allocate(hex.length / 2)
        var i = 0
        while (i < hex.length) {
            var hexStr = hex[i].toString() + ""
            i++
            hexStr += hex[i]
            val b = hexStr.toInt(16).toByte()
            bf.put(b)
            i++
        }
        return bf.array()
    }


    /**
     * 解密
     *
     * @param key       密钥
     * @param encrypted 待揭秘文本
     * @return
     */
    @Throws(Exception::class)
    fun decrypt(key: String, encrypted: String): String {
        val rawKey = toMakeKey(key)  // key.getBytes();
        val enc = toByte(encrypted)
        // enc = Base64.decode(enc, Base64.DEFAULT);
        val result = decrypt(rawKey, enc)
        // /result = Base64.decode(result, Base64.DEFAULT);
        return result.toString()
    }


    /**
     * 真正的加密过程
     * 1.通过密钥得到一个密钥专用的对象SecretKeySpec
     * 2.Cipher 加密算法，加密模式和填充方式三部分或指定加密算 (可以只用写算法然后用默认的其他方式)Cipher.getInstance("AES");
     *
     * @param key
     * @param src
     * @return
     */
    @Throws(Exception::class)
    private fun encrypt(key: ByteArray, src: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(src)
    }

    /**
     * 真正的加密过程
     */
    @Throws(Exception::class)
    private fun encrypt2Java(key: ByteArray, src: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(src)
    }

    /**
     * 真正的解密过程
     */
    @Throws(Exception::class)
    private fun decrypt(key: ByteArray, encrypted: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(encrypted)
    }

    /* fun toHex(txt: String): String {
         return toHex(txt.toByteArray())
     }

     fun fromHex(hex: String): String {
         return String(toByte(hex))
     }*/

    /**
     * 把16进制转化为字节数组
     */
    private fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).toByte()
        return result
    }

    /**
     * 二进制转字符,转成了16进制
     * 0123456789abcdefg
     */
    private fun toHex(buf: ByteArray): String {
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[b.toInt() shr 4 and 0x0f]).append(HEX[b.toInt() and 0x0f])
    }

    /**
     * 初始化 AES Cipher
     */
    private fun initAESCipher(sKey: String, cipherMode: Int): Cipher? {
        // 创建Key gen
        // KeyGenerator keyGenerator = null;
        var cipher: Cipher? = null
        try {
            /*
         * keyGenerator = KeyGenerator.getInstance("AES");
         * keyGenerator.init(128, new SecureRandom(sKey.getBytes()));
         * SecretKey secretKey = keyGenerator.generateKey(); byte[]
         * codeFormat = secretKey.getEncoded(); SecretKeySpec key = new
         * SecretKeySpec(codeFormat, "AES"); cipher =
         * Cipher.getInstance("AES"); //初始化 cipher.init(cipherMode, key);
         */
            val rawKey = toMakeKey(sKey)
            val skeySpec = SecretKeySpec(rawKey, "AES")
            cipher = Cipher.getInstance("AES")
            cipher.init(cipherMode, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        } catch (e: Exception) {
            e.printStackTrace() // To change body of catch statement use File |
        }
        return cipher
    }

    /**
     * 对文件进行AES加密
     */
    fun encryptFile(sourceFile: File?, toFile: String, dir: String, sKey: String): File? {
        // 新建临时加密文件
        var encrypfile: File? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = FileInputStream(sourceFile)
            encrypfile = File(dir + toFile)
            outputStream = FileOutputStream(encrypfile)
            val cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE)
            // 以加密流写入文件
            val cipherInputStream = CipherInputStream(inputStream, cipher)
            val cache = ByteArray(1024)
            var nRead = 0
            while (cipherInputStream.read(cache).also { nRead = it } != -1) {
                outputStream.write(cache, 0, nRead)
                outputStream.flush()
            }
            cipherInputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace() // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (e: IOException) {
            e.printStackTrace() // To change body of catch statement use File |
            // Settings | File Templates.
        } finally {
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace() // To change body of catch statement use
                // File | Settings | File Templates.
            }
            try {
                outputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace() // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
        return encrypfile
    }

    /**
     * AES方式解密文件
     *
     * @param sourceFile
     * @return
     */
    fun decryptFile(sourceFile: File?, toFile: String, dir: String, sKey: String): File? {
        var decryptFile: File? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            decryptFile = File(dir + toFile)
            val cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE)
            inputStream = FileInputStream(sourceFile)
            outputStream = FileOutputStream(decryptFile)
            val cipherOutputStream = CipherOutputStream(outputStream, cipher)
            val buffer = ByteArray(1024)
            var r: Int
            while (inputStream.read(buffer).also { r = it } >= 0) {
                cipherOutputStream.write(buffer, 0, r)
            }
            cipherOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace() // To change body of catch statement use File |
            // Settings | File Templates.
        } finally {
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace() // To change body of catch statement use
                // File | Settings | File Templates.
            }
            try {
                outputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace() // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
        return decryptFile
    }

    /**
     * 解密 带偏移量
     */
    @Throws(Exception::class)
    fun decodeCBCSync(data: String, key: String, offset: String): String {
        val key2 = toMakeKey(key)
        val iv = offset.toByteArray()
        // 初始化
        //Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        val secretKey: Key = SecretKeySpec(key2, "AES")
        // 初始化cipher
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encryptedText = cipher.doFinal(toByte(data))
        return String(encryptedText, StandardCharsets.UTF_8) //此处使用BASE64做转码。
    }

    /**
     * 补足key 16的倍数位
     *
     * @param key 补足key
     * @return 补全后的key byte[]
     */
    private fun toMakeKey(key: String): ByteArray {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        val base = 16
        val keyBytes = key.toByteArray()
        return if (keyBytes.size % base != 0) {
            val groups = keyBytes.size / base + if (keyBytes.size % base != 0) 1 else 0
            val temp = ByteArray(groups * base)
            Arrays.fill(temp, 0.toByte())
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.size)
            temp
        } else keyBytes
    }
}