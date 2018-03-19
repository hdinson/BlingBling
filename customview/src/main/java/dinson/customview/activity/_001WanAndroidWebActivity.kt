package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import dinson.customview.R
import dinson.customview._global.BaseActivity
import kotlinx.android.synthetic.main.activity__001_wan_android_web.*

class _001WanAndroidWebActivity : BaseActivity() {

    companion object {
        private const val EXTRA_LINK = "link"
        fun start(context: Context, link: String) {
            val intent = Intent(context, _001WanAndroidWebActivity::class.java)
            intent.putExtra(EXTRA_LINK, link)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__001_wan_android_web)


        initWeb()
        initData()
    }

    private fun initWeb() {
        wvContent.overScrollMode = View.OVER_SCROLL_NEVER
        wvContent.scrollBarStyle = View.SCROLLBARS_OUTSIDE_INSET
        val webSettings = wvContent.settings
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN     //自适应屏幕
    }

    private fun initData() {
        val link = intent.getStringExtra(EXTRA_LINK)
        wvContent.loadUrl(link)
    }


}
