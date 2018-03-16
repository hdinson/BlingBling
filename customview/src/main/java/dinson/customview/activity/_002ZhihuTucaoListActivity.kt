package dinson.customview.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
import dinson.customview.utils.SystemBarModeUtils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity__002_zhihu_tucao_list.*

class _002ZhihuTucaoListActivity : BaseActivity() {

    private lateinit var mData: ArrayList<ZhihuTucao>
    private lateinit var mAdapter: _002ZhihuListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__002_zhihu_tucao_list)

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
        rvContent.adapter = mAdapter
        rvContent.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener({ getServiceData() })
        swipeRefreshLayout.post({ swipeRefreshLayout.isRefreshing = true })
    }

    /**
     * 获取服务器数据
     */
    private fun getServiceData() {
        HttpHelper.create(ZhihuTucaoApi::class.java).getStoriesList()
            .compose(RxSchedulers.io_main())
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

                override fun onSubscribe(d: Disposable?) {
                    swipeRefreshLayout.isRefreshing = false
                }
            })
    }


}
