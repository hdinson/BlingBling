package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.ZhihuTucaoApi
import dinson.customview.entity.zhihu.ZhihuTucaoListResponse
import dinson.customview.http.BaseObserver
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.utils.SystemBarModeUtils
import kotlinx.android.synthetic.main.activity__002_zhihu_tucao_list.*

class _002ZhihuTucaoListActivity : BaseActivity() {

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

        swipeRefreshLayout.setOnRefreshListener({ getServiceData() })
        swipeRefreshLayout.post({ swipeRefreshLayout.isRefreshing = true })
    }

    /**
     * 获取服务器数据
     */
    private fun getServiceData() {
        HttpHelper.create(ZhihuTucaoApi::class.java).getStoriesList()
            .compose(RxSchedulers.io_main())
            .subscribe(object: BaseObserver<ZhihuTucaoListResponse?>() {
                override fun onHandlerSuccess(value: ZhihuTucaoListResponse) {

                }
            })
    }


}
