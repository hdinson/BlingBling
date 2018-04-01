package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import com.google.gson.Gson
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.ZhihuTucaoApi
import dinson.customview.db.AppDbUtils
import dinson.customview.db.model.ZhihuTucao
import dinson.customview.entity.zhihu.ZhihuTucaoDetails
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.error
import dinson.customview.utils.GlideUtils
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity__002_zhihu_tucao_content.*

class _002ZhihuTucaoContentActivity : BaseActivity() {

    companion object {
        private const val EXTRA_NEWS_BEAN = "news_bean"
        fun start(context: Context, bean: ZhihuTucao) {
            val intent = Intent(context, _002ZhihuTucaoContentActivity::class.java)
            intent.putExtra(EXTRA_NEWS_BEAN, bean)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__002_zhihu_tucao_content)
        SystemBarModeUtils.darkMode(this, true)

        initUI()
        initData()
    }

    private fun initUI() {
        //初始化
        val ivHeight = ivImg.layoutParams.height

        wvContent.overScrollMode = View.OVER_SCROLL_NEVER
        wvContent.scrollBarStyle = View.SCROLLBARS_OUTSIDE_INSET
        val webSettings = wvContent.settings
        //webSettings.javaScriptEnabled = true
        //自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        wvContent.setOnscrollChangeCallback { _, t, _, _ ->
            if (t > ivHeight * 1.5) return@setOnscrollChangeCallback

            val layoutParams = ivImg.layoutParams
            layoutParams.height = ivHeight - t
            val alphaTv = (ivHeight - t * 1.2f) / ivHeight
            tvTitle.alpha = alphaTv
            if (layoutParams.height < 0) {
                layoutParams.height = 0
            }
            ivImg.layoutParams = layoutParams
        }
    }

    private fun initData() {
        val extraData = intent.getParcelableExtra<ZhihuTucao>(EXTRA_NEWS_BEAN)
        val mData = AppDbUtils.queryById(extraData.id)
        if (mData == null) {
            onBackPressed()
            error("数据库查不到当前日期的数据")
            return
        }
        val content = mData.content
        if (content == null || content.isEmpty()) {
            //加载网络
            HttpHelper.create(ZhihuTucaoApi::class.java).getStoriesDetails(extraData.id)
                .compose(RxSchedulers.io_main())
                .subscribe({
                    //本地数据库存储
                    mData.content = it.string()
                    AppDbUtils.updateZhihuTucao(mData)

                    //解析数据，设置数据
                    parseJsonAndSetData(mData.content)
                }, {
                    error("知乎请求详情数据失败")
                    onBackPressed()
                })
        } else {
            //加载本地数据
            parseJsonAndSetData(content)
        }
    }

    /**
     * json解析
     */
    private fun parseJsonAndSetData(json: String) {
        val bean = Gson().fromJson(json, ZhihuTucaoDetails::class.java)
        //添加css关联
        val builder = StringBuilder()
        bean.css.forEach {
            builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"$it\" />")
        }
        builder.append(bean.body)
        //加载图片
        GlideUtils.setImage(this, bean.image, ivImg)
        tvTitle.text = bean.image_source
        wvContent.loadDataWithBaseURL(null, builder.toString(),
            "text/html", "utf-8", null)
    }
}