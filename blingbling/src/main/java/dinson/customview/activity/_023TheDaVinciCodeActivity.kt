package dinson.customview.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.kotlin.click
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.utils.steganography.decoding.DecodingCallback
import dinson.customview.utils.steganography.decoding.DecodingTask
import dinson.customview.utils.steganography.encoding.EncodingCallback
import dinson.customview.utils.steganography.encoding.EncodingTask
import dinson.customview.utils.steganography.model.ImageSteganography
import kotlinx.android.synthetic.main.activity__023_the_da_vinci_code.*


class _023TheDaVinciCodeActivity : BaseActivity() {

    companion object {
        private const val secret_key = "heheda"
    }

    private var mStartTime: Long = 0L
    private var mCurrentBitmap: Bitmap? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__023_the_da_vinci_code)

        initUI()

        btnEncode.click { _ ->
            val bitmapDrawable = ContextCompat.getDrawable(this, R.drawable._023_act_bg) as BitmapDrawable
            val imageSteganography = ImageSteganography(etSecret.text.toString(), secret_key, bitmapDrawable.bitmap)
            val textEncoding = EncodingTask(EncodingCallback {
                logi("onCompleteEncoding")
                if (it.isSuccess) {
                    logi("有图片")
                    tvResult.text = "加密成功\n"
                    tvResult.append("耗时：${System.currentTimeMillis() - mStartTime} ms")
                    mCurrentBitmap = it.encodedImage
                } else {
                    loge("失败")
                }
            })
            mStartTime = System.currentTimeMillis()
            textEncoding.execute(imageSteganography)
        }

        btnDecode.click { _ ->
            val steganography = ImageSteganography(secret_key, mCurrentBitmap)
            val textDecoding = DecodingTask(DecodingCallback {
                if (it.isSuccess) {
                    tvResult.text = "解码成功\n"
                    tvResult.append("耗时：${System.currentTimeMillis() - mStartTime} ms length: ${it.msg.length}")
                    logi(it.msg)
                } else {
                    loge(it.msg)
                }
            })
            mStartTime = System.currentTimeMillis()
            textDecoding.execute(steganography)
        }
    }


    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    /*private fun ImageView.getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            if (drawable.opacity != PixelFormat.OPAQUE)
                Bitmap.Config.ARGB_8888
            else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }*/
}
