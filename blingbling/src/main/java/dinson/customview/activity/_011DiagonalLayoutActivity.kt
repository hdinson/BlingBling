package dinson.customview.activity

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.dinson.blingbase.utils.FileUtils
import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.GlideUtils
import kotlinx.android.synthetic.main.activity__011_diagonal_layout.*

class _011DiagonalLayoutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__011_diagonal_layout)
        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        val hearUrl = "${BuildConfig.QINIU_URL}FtT8Kk7HNbHE5FLf3U2dnXkOZtu7.jpeg"
        GlideUtils.setImage(this, hearUrl, ivImg)
        val imgUrl = "${BuildConfig.QINIU_URL}FoPzP9JbDTqxMlhWCRvxPUo24IRn.webp"
        GlideUtils.setCircleImage(this, imgUrl, author)

        tvTitle.text = "Quan Zhou\n"
        val builder = SpannableStringBuilder("Eastern Asia culture")
        builder.setSpan(RelativeSizeSpan(0.56f), 0, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(StyleSpan(Typeface.BOLD), 0, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvTitle.append(builder)


        val qzStr = FileUtils.getTextFromAssets(this, "QuanZhou")
        tvContent.text = Html.fromHtml(qzStr)
    }
}
