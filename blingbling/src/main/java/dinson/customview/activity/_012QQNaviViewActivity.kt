package dinson.customview.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide

import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.LogUtils
import dinson.customview.weight._012qqnaviview.QQNaviView
import dinson.customview.weight._012qqnaviview.QQNaviViewManager
import kotlinx.android.synthetic.main.activity__012_qqnavi_view.*

class _012QQNaviViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__012_qqnavi_view)

        initUI()
    }

    private fun initUI() {
        val imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Fm71mrw6NECZAitx83iMnoBXUvgW.jpg"
        Glide.with(this).load(imgUrl).into(ivBg)


        val manager = QQNaviViewManager(qqViewBubble, qqViewPerson, qqViewStar)
        qqViewBubble.setOnClickListener {
            LogUtils.e("点击事件")
            manager.setCheckedView(it as QQNaviView) }
        qqViewPerson.setOnClickListener { manager.setCheckedView(it as QQNaviView) }
        qqViewStar.setOnClickListener { manager.setCheckedView(it as QQNaviView) }
    }

}
