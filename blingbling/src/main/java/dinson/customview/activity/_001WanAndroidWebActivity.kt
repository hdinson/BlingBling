package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import com.dinson.blingbase.utils.SystemBarModeUtils
import com.just.agentweb.AgentWeb
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.NetworkUtils
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

    private lateinit var mAgentWeb: AgentWeb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__001_wan_android_web)
        SystemBarModeUtils.setPaddingTop(this, flContainer)
        SystemBarModeUtils.darkMode(this, true)

        WebView.setWebContentsDebuggingEnabled(false)
        initData()
    }

    private fun initData() {
        val link = intent.getStringExtra(EXTRA_LINK)

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(flContainer, ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(link)
        mAgentWeb.webCreator.webView.apply {
            overScrollMode = View.OVER_SCROLL_NEVER
            //根据cache-control决定是否从网络上取数据。没网，则从本地获取，即离线加载
            settings.cacheMode = if (NetworkUtils.isNetworkAvailable(this@_001WanAndroidWebActivity)) WebSettings.LOAD_DEFAULT
            else WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
    }

    override fun onBackPressed() {
        if (!mAgentWeb.back())
            super.onBackPressed()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}