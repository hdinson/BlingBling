package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._002ZhihuListAdapter
import dinson.customview.api.ZhihuTucaoApi
import dinson.customview.db.AppDbUtils
import dinson.customview.db.model.ZhihuTucao
import dinson.customview.entity.zhihu.ZhihuTucaoListResponse
import dinson.customview.http.BaseObserver
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.debug
import dinson.customview.utils.DateUtils
import dinson.customview.utils.SystemBarModeUtils
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


        mData = AppDbUtils.getLocalDatas() as ArrayList<ZhihuTucao>
        mAdapter = _002ZhihuListAdapter(this, mData)

        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData()
            }

            override fun onLoadMore() {
                loadMoreData()
            }
        })
    }

    /**
     * 获取服务器数据
     */
    private fun getServiceData() {
        mZhihuTucaoApi.getStoriesList().compose(RxSchedulers.io_main())
            .subscribe(object : BaseObserver<ZhihuTucaoListResponse?>() {
                override fun onHandlerSuccess(response: ZhihuTucaoListResponse) {
                    //数据集不为空时，如果第一条的日期比较大时，说明没有更新的
                    if (mData.isNotEmpty() && mData[0].date >= response.stories[0].date) return

                    val map = response.stories.map { it.convertToZhihuTucao() }
                    AppDbUtils.insertMultZhihuTucao(map)
                    mData.clear()
                    mData.addAll(map)
                    mAdapter.notifyDataSetChanged()
                }
            })
    }

    /**
     * 加载更多数据
     */
    private fun loadMoreData() {
        //本地查询
        val datas = AppDbUtils.getLocalDatasBefore(mData.last().date)
        //显示本地数据
        if (datas.isNotEmpty()) {
            debug("本地有更多数据")
            val index = mData.size
            mData.addAll(datas)
            mAdapter.notifyItemChanged(index)
            return
        }
        //请求网络加载更多数据
        debug("请求网络加载更多数据")
        //拼接URL
        if (mData.isEmpty()) return
        val timestamp = DateUtils.str2int(mData.last().date.toString(), "yyyyMMdd")
        mZhihuTucaoApi.getStoriesListBeforeData(timestamp).compose(RxSchedulers.io_main())
            .subscribe(object : BaseObserver<ZhihuTucaoListResponse?>() {
                override fun onHandlerSuccess(response: ZhihuTucaoListResponse) {
                    val map = response.stories.map { it.convertToZhihuTucao() }
                    AppDbUtils.insertMultZhihuTucao(map)
                    mData.clear()
                    mData.addAll(map)
                    mAdapter.notifyDataSetChanged()
                }
            })
    }
}
