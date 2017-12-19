package dinson.customview.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.FileUtils
import dinson.customview.utils.GlideUtils
import kotlinx.android.synthetic.main.activity__011_diagonal_layout.*

class _011DiagonalLayoutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__011_diagonal_layout)

        initUI()
    }


    private fun initUI() {
        val hearUrl = "http://ondlsj2sn.bkt.clouddn.com/FtT8Kk7HNbHE5FLf3U2dnXkOZtu7.jpeg"
        GlideUtils.setImage(this, hearUrl, ivImg)
        val imgUrl = "http://ondlsj2sn.bkt.clouddn.com/FoPzP9JbDTqxMlhWCRvxPUo24IRn.webp"
        GlideUtils.setCircleImage(this, imgUrl, cvCircleView)


        tvTitle.text = "Quan Zhou\n"
        val builder = SpannableStringBuilder("Eastern Asia culture")
        builder.setSpan(RelativeSizeSpan(0.56f), 0, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(StyleSpan(Typeface.BOLD), 0, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvTitle.append(builder)


        val qzStr = FileUtils.getTextFromAssets(this, "QuanZhou")

        tvContent.text = Html.fromHtml(qzStr)
    }
}
