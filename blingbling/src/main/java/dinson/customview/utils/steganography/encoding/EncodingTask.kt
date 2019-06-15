package dinson.customview.utils.steganography.encoding

import android.os.AsyncTask

import dinson.customview.utils.steganography.model.ImageSteganography
import dinson.customview.utils.steganography.core.EncodeDecode
import dinson.customview.utils.steganography.util.SteganographyUtils

/**
 * In this class all those method in EncodeDecode class are used to encode secret message in image.
 * All the tasks will run in background.
 */
class EncodingTask(private val callbackInterface: EncodingCallback) : AsyncTask<ImageSteganography, Int, EncodingResult>() {

    override fun onPostExecute(result: EncodingResult) {
        super.onPostExecute(result)
        callbackInterface.onCompleteEncoding(result)
    }

    override fun doInBackground(vararg imageSteganographies: ImageSteganography): EncodingResult {

        val result = EncodingResult()
        if (imageSteganographies.isNotEmpty()) {

            val textSteganography = imageSteganographies[0]

            //获取源图片
            val bitmap = textSteganography.image

            //原始图片宽高
            val originalHeight = bitmap.height
            val originalWidth = bitmap.width

            //分割图片成的图像块
            val srcList = SteganographyUtils.splitImage(bitmap)

            //将加密的消息编码成图像
            val encodedList = EncodeDecode.encodeMessage(srcList, textSteganography.encryptMessage())

            //内存回收
            srcList.forEach { it.recycle() }
            System.gc()

            //所有块图像列表合并为一个单一图像
            val srcEncoded = SteganographyUtils.mergeImage(encodedList, originalHeight, originalWidth)

            //内存回收
            encodedList.forEach { it.recycle() }
            System.gc()

            result.encodedImage = srcEncoded
            result.isSuccess = true
        }
        return result
    }
}
