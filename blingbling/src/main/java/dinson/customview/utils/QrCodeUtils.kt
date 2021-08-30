package dinson.customview.utils

import android.graphics.*
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.security.SecureRandom
import java.util.*
import kotlin.collections.HashMap

object QrCodeUtils {


    fun createBindQr(url: String, width: Int, icBitmap: Bitmap? = null, imageScale: Float = 0.2f): Bitmap? {
        //算出来以设定宽度的圆形的内接正方形的宽度，就是二维码的宽度，二维码会被放在圆形背景上面
        //得到方形二维码的位图，并且计算了方形二位码的小格子的宽度和默认的margin以后，再绘制一个模拟的二维码，按照计算得到的方形二维码小格子的尺寸填充整个背景区域
        //然后把方形二维码贴在模拟二维码上
        //接着把logo贴上去
        //接着把图片切成圆形，然后绘制上圆形的边框

        //算出来以设定宽度的圆形的内接正方形的宽度，就是二维码的宽度，二维码会被放在圆形背景上面
        val targetWidth = (width * 1.4142f / 2).toInt()
        val params = IntArray(2) //这里0 保存的是获取的二维码小格子的宽度，1保存的是方形二维码的边框的宽度
        //生成二维码原图（步骤1）
        val qrBitmap = createQrBitmap(url, targetWidth, params)
        if (qrBitmap != null) {
            //生成一个最终要返回的位图
            val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            //根据步骤1返回的参数绘制有一个模拟的方形二维码
            drawDefQr(canvas, params[0], width)
            //生成的二维码的边框宽度
            val qrMargin = params[1]
            //要绘制的实际的二维码的宽度，去掉边框
            val qrWidth = targetWidth - params[1] * 2

            //待绘制二维码在背景中实际的左边距和上边距
            var drawMargin = (width - targetWidth) / 2
            //为了待位置二维码和作为背景的模拟二维码格子能无缝拼接，需要微调一下边距，确保边距是格子的整数倍
            drawMargin = wrapDrawMargin(drawMargin, params[0])
            //计算原二维码位图要绘制的区域，这个区域要排除边框的宽度
            val source = Rect(qrMargin, qrMargin, qrMargin + qrWidth, qrMargin + qrWidth)
            //计算待绘制的二维码位图在画布上的位置
            val target = Rect(drawMargin, drawMargin, drawMargin + targetWidth, drawMargin + targetWidth)
            //把实际的二维码绘制到画布上
            canvas.drawBitmap(qrBitmap, source, target, paint)

            //绘制图标
            if (icBitmap != null) {
                val icWidth = (width * imageScale).toInt()
                val margin = ((1 - imageScale) / 2f * width).toInt()
                val rect = Rect(margin, margin, margin + icWidth, margin + icWidth)
                canvas.drawBitmap(icBitmap, null, rect, paint)
            }

            //绘制圆形边框
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f
            val radius = width / 2f - 2.5f
            paint.color = Color.WHITE
            canvas.drawCircle(width / 2f, width / 2f, radius, paint)
            //把画布切成圆形
            return null
        }
        return null
    }

    /**
     * @param url      二维码的内容
     * @param width    二维码绘制的宽度
     * @param shortest 用来乘放二维码位图中相关的尺寸参数 0 保存的是获取的二维码小格子的宽度，1保存的是方形二维码的边框的宽度
     * @return 二维码位图，包含边框
     */
    private fun createQrBitmap(url: String, width: Int, shortest: IntArray): Bitmap? {
        return try {
            val hints = HashMap<EncodeHintType, Any>().apply {
                this[EncodeHintType.CHARACTER_SET] = "utf-8"
                this[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
                this[EncodeHintType.MARGIN] = 0
            }
            val byteMatrix = QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, width, hints)


            val pixels = IntArray(width * width)
            var shortestPix = 0
            var lastSelected = false
            var currentShortest = width
            var consequenceSelected = 0
            var tempForMargin = 0
            var allUnSelected = true


            for (y in 0 until width) {
                allUnSelected = true
                for (x in 0 until width) {
                    val selected = byteMatrix[x, y]
                    //把选中状态记录到bitmap中，用颜色来表示
                    pixels[y * width + x] = if (selected) Color.WHITE else Color.BLACK
                    //记录这一行是不是全部未选中，如果全部未选中，说明这一行是上边框或下边框，连续未选中行数可以确定上边框或下边框的宽度
                    if (selected) {
                        allUnSelected = false
                    }
                    //如果margin找到了，说明接下来的才是真正二维码的内容
                    if (shortest[1] > 0) {
                        //***********这些逻辑是为了找到单个二维码格子的宽度,最小的连续格子数就是二维码最小格子的宽度
                        //如果是第一列，记录初始的选中状态
                        if (x < shortest[1] || x >= width - shortest[1]) {
                            //排除掉横向在绘制margin的情况
                            continue
                        }
                        if (x == shortest[1]) { //在每行的第一个像素开始记录最初的宽度
                            lastSelected = selected
                            shortestPix = 1
                        } else if (selected == lastSelected) {
                            shortestPix++ //如果选中状态不变，说明是同一个格子
                        } else {
                            if (shortestPix > 1) {
                                if (shortestPix < currentShortest) {
                                    //如果有比上一次计算得到的更小的格子，就记录当前值
                                    currentShortest = shortestPix
                                }
                            }
                            lastSelected = selected
                            shortestPix = 1
                        }
                    }
                }
                if (consequenceSelected == 0 && allUnSelected) {
                    tempForMargin++
                } else if (tempForMargin > 0 && consequenceSelected == 0) {
                    consequenceSelected = tempForMargin
                    shortest[1] = consequenceSelected
                }
            }
            shortest[0] = currentShortest

            val findMinStep = findMinStep(pixels)

            println("1-> $findMinStep, 2->$currentShortest")

            val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, width)
            bitmap
        } catch (ignored: Exception) {
            null
        }
    }

    private fun findMinStep(@ColorInt pixels: IntArray): Int {
        var tempMin = 0
        var minStep = pixels.size
        pixels.forEach {
            val selector = it == Color.WHITE
            if (selector) {
                tempMin++
            } else {
                if (tempMin in 1 until minStep){
                    minStep = tempMin
                }
                tempMin = 0
            }
        }
        return minStep
    }


    /**
     * 绘制一个模拟的二维码，二维码的图案是随机填充的
     *
     * @param canvas
     * @param shortest 模拟二维码小格子的宽度
     * @param width    模拟二维码的宽度
     */
    private fun drawDefQr(canvas: Canvas, shortest: Int, width: Int) {
        val paint = Paint()
        val random: Random = SecureRandom()
        for (y in 0 until width step shortest) {
            for (x in 0 until width step shortest) {
                val select = random.nextInt(2)
                paint.color = if (select == 1) Color.BLACK else Color.WHITE
                canvas.drawRect(x.toFloat(), y.toFloat(), (x + shortest).toFloat(), (y + shortest).toFloat(), paint)
            }
        }
    }

    /**
     * 把left/top margin微调到格子的整数倍，以便模拟二维码和实际二维码之间没有缝隙
     */
    private fun wrapDrawMargin(sourceMargin: Int, cubSize: Int): Int {
        val left = sourceMargin % cubSize
        return sourceMargin - left
    }

}