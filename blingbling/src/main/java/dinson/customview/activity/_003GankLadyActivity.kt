package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.trello.rxlifecycle2.android.ActivityEvent
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._003GankGirlsPicAdapter
import dinson.customview.api.GankApi
import dinson.customview.entity.gank.GankRoot
import dinson.customview.entity.gank.Welfare
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.toast
import dinson.customview.model._003GankLadyMode
import dinson.customview.utils.SPUtils
import dinson.customview.utils.SystemBarModeUtils
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity__003_gank_lady.*

class _003GankLadyActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, _003GankLadyActivity::class.java))
        }
    }

    private lateinit var mCurrencyMode: _003GankLadyMode
    private var mCurrentPage = 1
    private val mPageSize = 20
    private val mApi = HttpHelper.create(GankApi::class.java)
    private val mRandomObservable by lazy { mApi.randomLadyPic(mPageSize) }

    private lateinit var mAdapter: _003GankGirlsPicAdapter
    private val mData = ArrayList<Welfare>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__003_gank_lady)

        initUI()
    }

    private fun initUI() {
        SystemBarModeUtils.setPaddingTop(this, toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mCurrencyMode = SPUtils.getGankLadyMode()
        mAdapter = _003GankGirlsPicAdapter(mData)

        flCustomRefreshView.recyclerView.layoutManager = GridLayoutManager(this, 2)
        flCustomRefreshView.setAdapter(mAdapter)
        flCustomRefreshView.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer(true)
            }

            override fun onLoadMore() {
                getDataFromServer(false)
            }
        })
        flCustomRefreshView.isRefreshing = true
        flCustomRefreshView.setEmptyView("")

        RvItemClickSupport.addTo(flCustomRefreshView.recyclerView)
            .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                mData[position].url.toast()
            })
    }

    private fun getDataFromServer(isRefresh: Boolean) {
        if (isRefresh) mCurrentPage = 1
        getObservable(mCurrentPage)
            .filter { !it.isError }
            .doOnNext { data ->
                if (isRefresh) return@doOnNext
                val baseHeight = 900
                data.results.forEach {
                    it.height = when (mCurrencyMode) {
                        _003GankLadyMode.NORMAL -> baseHeight
                        _003GankLadyMode.RANDOM -> baseHeight + (Math.random() * 100).toInt()
                    }
                }
            }
            .compose(RxSchedulers.io_main())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({
                flCustomRefreshView.complete()
                if (isRefresh) mData.clear()

                if (it.results == null || it.results.isEmpty())
                    flCustomRefreshView.onNoMore()

                mData.addAll(it.results)
                mAdapter.notifyDataSetChanged()
                mCurrentPage++
            }, {
                it.message?.loge()
                flCustomRefreshView.complete()
            }).addToManaged()

    }

    private fun getObservable(page: Int): Observable<GankRoot<ArrayList<Welfare>>> {
        return when (mCurrencyMode) {
            _003GankLadyMode.NORMAL -> mApi.gankIOGirlsPic(mPageSize, page)
            _003GankLadyMode.RANDOM -> mRandomObservable
        }
    }
}
