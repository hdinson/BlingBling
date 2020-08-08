package dinson.customview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.dinson.blingbase.utils.SystemBarModeUtils
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.adapter._027HomeAdapter
import dinson.customview.entity.av.Movie
import dinson.customview.kotlin.logi
import dinson.customview.model._027AvModel

import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity__027_movie_list_by_link.*
import java.util.ArrayList

class _027MovieListByLinkActivity : BaseActivity() {

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_LINK = "extra_link"
        fun start(context: Context, title: String, link: String) {
            val intent = Intent(context, _027MovieListByLinkActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_LINK, link)
            context.startActivity(intent)
        }
    }

    private var mPage = 1
    private val mDataList = ArrayList<Movie>()
    private var mAdapter: _027HomeAdapter? = null
    private var mLink = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__027_movie_list_by_link)

        SystemBarModeUtils.setPaddingTop(this, toolbar)

        toolbar.title = intent.getStringExtra(EXTRA_TITLE)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        mLink = intent.getStringExtra(EXTRA_LINK) ?: ""

        initUI()
    }

    private fun initUI() {


        mAdapter = _027HomeAdapter(mDataList)
        crvMovieContent.setAdapter(mAdapter)
        crvMovieContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData(true)
            }

            override fun onLoadMore() {
                getServiceData(false)
            }
        })
        crvMovieContent.isRefreshing = true
        crvMovieContent.setEmptyView("")
        crvMovieContent.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            RvItemClickSupport.addTo(this).setOnItemClickListener { _, _, pos ->
                if (pos >= 0 && pos < mDataList.size) {
                    _027MovieDetailsActivity.start(context, mDataList[pos])
                }
            }
        }
    }


    private fun getServiceData(isRefresh: Boolean) {
        mPage = if (isRefresh) 1 else mPage + 1
        val url = "$mLink/page/$mPage"
        logi { url }
        Observable.just(url)
            .map { _027AvModel.decodeMovie(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (isRefresh) {
                    mDataList.clear()
                }
                if (it.isEmpty() || it.size < 10) crvMovieContent.onNoMore()
                else crvMovieContent.onLoadingMore()
                mDataList.addAll(it)
                mAdapter?.notifyDataSetChanged()
                crvMovieContent.complete()
            }.addToManaged()
    }
}
