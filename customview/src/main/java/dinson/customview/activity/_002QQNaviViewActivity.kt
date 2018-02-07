package dinson.customview.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide

import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.LogUtils
import dinson.customview.weight._002qqnaviview.QQNaviView
import dinson.customview.weight._002qqnaviview.QQNaviViewManager
import kotlinx.android.synthetic.main.activity__002_qqnavi_view.*

class _002QQNaviViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__002_qqnavi_view)

        initUI()
    }

    private fun initUI() {
        val imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Fm71mrw6NECZAitx83iMnoBXUvgW.jpg"
        Glide.with(this).load(imgUrl).into(findViewById<View>(R.id.iv_bg) as ImageView)


        val manager = QQNaviViewManager(qqViewBubble, qqViewPerson, qqViewStar)
        qqViewBubble.setOnClickListener {
            LogUtils.e("点击事件")
            manager.setCheckedView(it as QQNaviView) }
        qqViewPerson.setOnClickListener { manager.setCheckedView(it as QQNaviView) }
        qqViewStar.setOnClickListener { manager.setCheckedView(it as QQNaviView) }
    }

}
