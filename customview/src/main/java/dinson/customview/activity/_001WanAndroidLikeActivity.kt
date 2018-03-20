package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._001WanAndroidLikeListAdapter
import dinson.customview.api.WanAndroidApi
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.activity__001_wan_android_like.*


class _001WanAndroidLikeActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, _001WanAndroidLikeActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var mWanAndroidApi: WanAndroidApi
    private lateinit var mAdapter: _001WanAndroidLikeListAdapter
    private val mData = ArrayList<WanAndArticle>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__001_wan_android_like)

        mWanAndroidApi = HttpHelper.create(WanAndroidApi::class.java)
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mAdapter = _001WanAndroidLikeListAdapter(this, mData)

        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData()
            }

            override fun onLoadMore() {}
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
    }

    /**
     * 获取文章数据
     */
    private fun getServiceData() {
        mWanAndroidApi.collectList().compose(RxSchedulers.io_main())
            .subscribe({
                flCustomRefreshView.complete()
                mData.clear()
                val article = it.data.datas
                flCustomRefreshView.onNoMore()

                mData.addAll(article)
                mAdapter.notifyDataSetChanged()
            }, {
                flCustomRefreshView.complete()
            })
    }
}
