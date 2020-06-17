package dinson.customview.utils.steganography.core

import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import com.dinson.blingbase.kotlin.logd
import com.dinson.blingbase.kotlin.logv
import dinson.customview.utils.steganography.model.MessageDecodingStatus
import dinson.customview.utils.steganography.model.MessageEncodingStatus
import dinson.customview.utils.steganography.util.SteganographyUtils
import java.nio.charset.Charset
import java.util.*

object EncodeDecode {

    private val binary = intArrayOf(16, 8, 0)
    private val toShift = intArrayOf(6, 4, 2, 0)
    private val andByte = byteArrayOf(0xC0.toByte(), 0x30, 0x0C, 0x03)

    //解码的标识符
    private const val START_MESSAGE_CONSTANT = "@!#"
    private const val END_MESSAGE_CONSTANT = "#!@"

    /**
     * 编码图像块
     *
     * @param   splittedImages {list of chunk images}
     * @param   encryptedMessage {string}
     * @return  Encoded list of chunk images
     */
    fun encodeMessage(splittedImages: List<Bitmap>, encryptedMessage: String): List<Bitmap> {
        val result = ArrayList<Bitmap>(splittedImages.size)

        //信息二进制编码并加入标识符
        val tmp = "$START_MESSAGE_CONSTANT$encryptedMessage$END_MESSAGE_CONSTANT"
        val byteMessage = tmp.toByteArray(Charset.forName("UTF-8"))
        val message = MessageEncodingStatus()
        message.message = encryptedMessage
        message.byteArrayMessage = byteMessage
        message.currentMessageIndex = 0
        message.isMessageEncoded = false

        logv("Message length :${byteMessage.size}")
        splittedImages.forEach {
            if (!message.isMessageEncoded) {

                //小图像块的宽高
                val width = it.width
                val height = it.height

                //打扁成一维数组
                val oneD = IntArray(width * height)
                it.getPixels(oneD, 0, width, 0, 0, width, height)

                //小图像块编码(核心)
                val encodedImage = encodeMessage(oneD, width, height, message)

                //将byte_image_array转换为integer_array
                val oneDMod = SteganographyUtils.byteArrayToIntArray(encodedImage)

                //绘制图像块
                val encodedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                encodedBitmap.density = it.density

                var masterIndex = 0

                //设置像素
                for (j in 0 until height)
                    for (i in 0 until width) {
                        encodedBitmap.setPixel(i, j, Color.argb(0xFF,
                            oneDMod[masterIndex] shr 16 and 0xFF,
                            oneDMod[masterIndex] shr 8 and 0xFF,
                            oneDMod[masterIndex++] and 0xFF))
                    }
                result.add(encodedBitmap)
            } else {
                //添加剩余图像块
                result.add(it.copy(it.config, false))
            }
        }
        return result
    }

    /**
     * 小图像块编码
     *
     * @param  intPixelArray {The integer RGB array}
     * @param  image_columns {Image width}
     * @param  image_rows {Image height}
     * @param  messageEncodingStatus {object}
     * @return   byte encoded pixel array
     */
    private fun encodeMessage(intPixelArray: IntArray, image_columns: Int, image_rows: Int,
                              messageEncodingStatus: MessageEncodingStatus): ByteArray {

        //RGB通道
        val channels = 3
        var shiftIndex = 4
        val result = ByteArray(image_rows * image_columns * channels)
        var resultIndex = 0
        for (row in 0 until image_rows) {
            for (col in 0 until image_columns) {
                //1D转2D
                val element = row * image_columns + col
                var tmp: Byte
                for (channelIndex in 0 until channels) {

                    if (!messageEncodingStatus.isMessageEncoded) {

                        //将整数值左移2，并用message_byte_array值替换两个最低有效数字
                        val pixel = intPixelArray[element] shr binary[channelIndex] and 0xFF and 0xFC
                        val digit = toShift[shiftIndex++ % toShift.size]
                        val msg = messageEncodingStatus.byteArrayMessage[messageEncodingStatus.currentMessageIndex].toInt() shr digit and 0x3
                        tmp = (pixel or msg).toByte()

                        if (shiftIndex % toShift.size == 0) {
                            messageEncodingStatus.incrementMessageIndex()
                        }
                        if (messageEncodingStatus.currentMessageIndex == messageEncodingStatus.byteArrayMessage.size) {
                            messageEncodingStatus.isMessageEncoded = true
                        }
                    } else {
                        //只需将整数复制到结果数组
                        tmp = (intPixelArray[element] shr binary[channelIndex] and 0xFF).toByte()
                    }
                    result[resultIndex++] = tmp
                }
            }
        }
        return result
    }


    /**
     *该方法采用编码块图像的列表并对其进行解码
     *
     * @return  encrypted message {String}
     * @param   encodedImages {list of encode chunk images}
     */
    fun decodeMessage(encodedImages: List<Bitmap>): String {
        //解码model
        val messageDecodingStatus = MessageDecodingStatus()
        encodedImages.forEach {
            val pixels = IntArray(it.width * it.height)
            it.getPixels(pixels, 0, it.width, 0, 0, it.width, it.height)

            //将表示[argb]值的整数数组转换为字节数组
            val b = SteganographyUtils.convertArray(pixels)
            //开始解码
            decodeMessage(b, messageDecodingStatus)
            //优化判断
            if (messageDecodingStatus.isEnded)
                return@forEach
        }
        if (!TextUtils.isEmpty(messageDecodingStatus.message)) {
            //去除标识符
            messageDecodingStatus.message = messageDecodingStatus.message.substring(START_MESSAGE_CONSTANT.length
                , messageDecodingStatus.message.length - END_MESSAGE_CONSTANT.length)
        }
        return messageDecodingStatus.message
    }

    /**
     * 解码图像块
     *
     * @param   bytePixelArray  {argb}字节数组
     * @param   messageDecodingStatus {object}
     * @return   Void
     */
    private fun decodeMessage(bytePixelArray: ByteArray, messageDecodingStatus: MessageDecodingStatus) {
        var shiftIndex = 4
        var tmp: Byte = 0x00

        bytePixelArray.forEachIndexed { index, _ ->
            //从bytePixelArray获取最后两位
            val i = bytePixelArray[index].toInt() shl toShift[shiftIndex % toShift.size]
            tmp = (i and andByte[shiftIndex++ % toShift.size].toInt() or tmp.toInt()).toByte()

            if (shiftIndex % toShift.size == 0) {
                //byte转string
                val str = String(byteArrayOf(tmp), Charset.forName("UTF-8"))

                if (messageDecodingStatus.message.endsWith(END_MESSAGE_CONSTANT)) {
                    logd("解码结束")
                    messageDecodingStatus.isEnded = true
                    return
                } else {
                    //添加信息
                    messageDecodingStatus.message = messageDecodingStatus.message + str
                    //如果那里没有消息，并且只有开始和结束消息常量在那里
                    if (messageDecodingStatus.message.length == START_MESSAGE_CONSTANT.length &&
                        START_MESSAGE_CONSTANT != messageDecodingStatus.message) {
                        messageDecodingStatus.isEnded = true
                        return
                    }
                }
                tmp = 0x00
            }
        }
    }
}