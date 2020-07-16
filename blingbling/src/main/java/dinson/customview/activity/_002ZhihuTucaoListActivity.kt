package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._002ZhihuListAdapter
import dinson.customview.api.ZhihuTucaoApi
import dinson.customview.db.ZhiHuDbUtils
import dinson.customview.db.model.ZhihuTucao
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.logd
import com.dinson.blingbase.utils.DateUtils
import dinson.customview.utils.SystemBarModeUtils
import com.dinson.blingbase.widget.recycleview.OnRvItemClickListener
import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.activity__002_zhihu_tucao_list.*


class _002ZhihuTucaoListActivity : BaseActivity() {

    private lateinit var mData: ArrayList<ZhihuTucao>
    private lateinit var mAdapter: _002ZhihuListAdapter
    private lateinit var mZhihuTucaoApi: ZhihuTucaoApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__002_zhihu_tucao_list)

        mZhihuTucaoApi = HttpHelper.create(ZhihuTucaoApi::class.java)
        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mData = ZhiHuDbUtils.getLocalDatas() as ArrayList<ZhihuTucao>
        mAdapter = _002ZhihuListAdapter(mData)

        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {

            override fun onRefresh() {
                getServiceData()
            }

            override fun onLoadMore() {
                loadMoreData()
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")
        RvItemClickSupport.addTo(flCustomRefreshView.recyclerView)
            .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                _002ZhihuTucaoContentActivity.start(this, mData[position])
            })
    }

    //

    /**
     * 获取服务器数据
     */
    private fun getServiceData() {
        mZhihuTucaoApi.getStoriesList()
            .compose(RxSchedulers.io_main())
            .subscribe({ response ->
                //数据集不为空时，如果第一条的日期比较大时，说明没有更新的
                if (mData.isNotEmpty() && mData[0].date >= response.stories[0].date)
                    return@subscribe

                val map = response.stories.map { it.convertToZhihuTucao() }
                ZhiHuDbUtils.insertMultiZhihuTucao(map)
                mData.clear()
                mData.addAll(map)
                mAdapter.notifyDataSetChanged()
            }, {
                flCustomRefreshView.complete()
            }, {
                flCustomRefreshView.complete()
            }).addToManaged()
    }

    /**
     * 加载更多数据
     */
    private fun loadMoreData() {
        //本地查询
        val data = ZhiHuDbUtils.getLocalDataBefore(mData.last().date)
        //显示本地数据
        if (data.isNotEmpty()) {
            logd{"本地有更多数据"}
            val index = mData.size - 2
            mData.addAll(data)
            mAdapter.notifyItemChanged(index)
            flCustomRefreshView.complete()
            return
        }
        //请求网络加载更多数据
        logd{"请求网络加载更多数据"}
        //拼接URL
        if (mData.isEmpty()) return
        var timestamp = DateUtils.str2int(mData.last().date.toString(), "yyyyMMdd")
        timestamp -= 3600
        mZhihuTucaoApi.getStoriesListBeforeData(timestamp)
            .compose(RxSchedulers.io_main())
            .subscribe({ response ->
                if (response.stories.isEmpty()) {
                    //没有更多的数据了
                    flCustomRefreshView.onNoMore()
                    return@subscribe
                }
                val map = response.stories.map { it.convertToZhihuTucao() }
                ZhiHuDbUtils.insertMultiZhihuTucao(map)
                val index = mData.size - 2
                mData.addAll(map)
                mAdapter.notifyItemChanged(index)
            }, {
                flCustomRefreshView.complete()
            }, {
                flCustomRefreshView.complete()
            }).addToManaged()
    }

    override fun setWindowBackgroundColor() = R.color._002_window_bg
}
