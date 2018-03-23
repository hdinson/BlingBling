package dinson.customview.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dinson.customview.R
import dinson.customview._global.BaseActivity
import kotlinx.android.synthetic.main.activity__021_like_smile_view.*

class _021LikeSmileViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__021_like_smile_view)
        initUI()
    }

    private fun initUI() {
        tvBack.setOnClickListener { onBackPressed() }
        //顶部图片
        val imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Ft8BnkO6CU83GxS2ZaSWXtMc_AQm.webp"
        val rOptions = RequestOptions().error(R.drawable.def_img)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
        val tOptions = DrawableTransitionOptions().crossFade(500)
        Glide.with(this).load(imgUrl).transition(tOptions).apply(rOptions).into(ivImg)

        likeSmile.setNum(66, 25)
    }

    override fun onDestroy() {
        super.onDestroy()
        likeSmile.onDestroy()
    }
}
