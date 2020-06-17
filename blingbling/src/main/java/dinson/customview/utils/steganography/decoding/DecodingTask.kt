package dinson.customview.utils.steganography.decoding

import android.os.AsyncTask
import android.text.TextUtils
import com.dinson.blingbase.kotlin.logv
import dinson.customview.utils.steganography.model.ImageSteganography
import dinson.customview.utils.steganography.util.SteganographyUtils
import dinson.customview.utils.steganography.core.EncodeDecode

/**
 * In this class all those method in EncodeDecode class are used to decode secret message in image.
 * All the tasks will run in background.
 */
class DecodingTask(private val textDecodingCallback: DecodingCallback) : AsyncTask<ImageSteganography, Void, DecodingResult>() {

    override fun onPostExecute(result: DecodingResult) {
        super.onPostExecute(result)
        textDecodingCallback.onCompleteEncoding(result)
    }

    override fun doInBackground(vararg imageSteganographies: ImageSteganography): DecodingResult {
        val result = DecodingResult()

        if (imageSteganographies.isNotEmpty()) {
            val imageSteganography = imageSteganographies[0]

            //获取源图片
            val bitmap = imageSteganography.image

            //分割图片成的图像块
            val srcEncodedList = SteganographyUtils.splitImage(bitmap)
            //解析图像块存储信息
            val decodedMessage = EncodeDecode.decodeMessage(srcEncodedList)
            logv("图像块存储信息 : $decodedMessage")
            if (TextUtils.isEmpty(decodedMessage)) {
                result.msg = "图像无信息"
                return result
            }
            //解析原始信息
            val decryptedMessage = imageSteganography.decryptMessage(decodedMessage)
            logv("原始信息 : $decryptedMessage")
            if (TextUtils.isEmpty(decryptedMessage)) {
                result.msg = "密钥出错"
                return result
            }
            //释放内存
            srcEncodedList.forEach { it.recycle() }
            System.gc()
            result.isSuccess = true
            result.msg = decryptedMessage
        }
        return result
    }
}
