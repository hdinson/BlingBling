package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._001WanAndroidMainListAdapter
import dinson.customview.api.WanAndroidApi
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.activity__002_zhihu_tucao_list.*

class _001WanAndroidActivity : BaseActivity() {

    private lateinit var mWanAndroidApi: WanAndroidApi
    private lateinit var mAdapter: _001WanAndroidMainListAdapter
    private val mData = ArrayList<WanAndArticle>()
    private var mPageIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__001_wan_android)

        mWanAndroidApi = HttpHelper.create(WanAndroidApi::class.java)
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mAdapter = _001WanAndroidMainListAdapter(this, mData)

        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData(true)
            }

            override fun onLoadMore() {
                getServiceData(false)
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
        flCustomRefreshView.recyclerView.addItemDecoration(LinearItemDecoration(this))
    }

    /**
     * 获取文章数据
     * @param isRefresh 是否刷新
     */
    private fun getServiceData(isRefresh: Boolean) {
        if (isRefresh) mPageIndex = 0
        mWanAndroidApi.getMainArticleList(mPageIndex).compose(RxSchedulers.io_main())
            .subscribe({
                flCustomRefreshView.complete()
                if (isRefresh) mData.clear()
                val article = it.data.datas
                if (article.isEmpty()) flCustomRefreshView.onNoMore()

                mData.addAll(article)
                mAdapter.notifyDataSetChanged()
                mPageIndex++
            }, {
                flCustomRefreshView.complete()
            })
    }
}
