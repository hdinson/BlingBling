package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import dinson.customview.R
import dinson.customview._global.BaseFragment
import dinson.customview.adapter._003GankGirlsPicAdapter
import dinson.customview.api.GankApi
import dinson.customview.entity.gank.Welfare
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.logd
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.toast
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.fragment_003_girl_pic_set.*

/**
 * Gank妹子界面
 */
class _003GirlGankFragment : BaseFragment() {

    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        "GankFragment onCreateView".logd()
        return original.inflate(R.layout.fragment_003_girl_pic_set, container, false)
    }

    private var mAdapter: _003GankGirlsPicAdapter? = null
    private val mData = ArrayList<Welfare>()
    private var mCurrentPage = 1
    private val mPageSize = 20
    private val mApi by lazy {
        HttpHelper.create(GankApi::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "GankFragment onViewCreated".logd()
        mAdapter = _003GankGirlsPicAdapter(mData)
        crfGirlsContent.setAdapter(mAdapter)
        crfGirlsContent.setEmptyView("")
    }


    override fun onResumeLazyLoad(): Boolean {

        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        crfGirlsContent.recyclerView.layoutManager = manager

        crfGirlsContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer(true)
            }

            override fun onLoadMore() {
                getDataFromServer(false)
            }
        })
        crfGirlsContent.isRefreshing = true

        RvItemClickSupport.addTo(crfGirlsContent.recyclerView)
            .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                mData[position].url.toast()
            })
        return true
    }

    private fun getDataFromServer(isRefresh: Boolean) {
        if (isRefresh) mCurrentPage = 1
        mApi.gankIOGirlsPic(mPageSize, mCurrentPage)
            .doOnNext { data ->
                data.results.forEach {
                    try {
                        val bitmap = Glide.with(this).asBitmap().load(it.url)
                            .submit().get()
                        if (bitmap != null) {
                            it.width = bitmap.width
                            it.height = bitmap.height
                        }
                    } catch (e: Exception) {
                    }
                }
            }
            .compose(RxSchedulers.io_main())
            .subscribe({
                crfGirlsContent.complete()
                if (isRefresh) mData.clear()

                if (it.results == null || it.results.isEmpty())
                    crfGirlsContent.onNoMore()

                mData.addAll(it.results)
                mAdapter?.notifyDataSetChanged()
                mCurrentPage++
            }, {
                it.message?.loge()
                crfGirlsContent.complete()
            }).addToManaged()

    }

    override fun onResume() {
        super.onResume()
        "GankFragment onResume".logd()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "GankFragment onCreate".logd()
    }


    override fun onDestroy() {
        super.onDestroy()
        "GankFragment onDestroy".logd()
    }

    override fun onPause() {
        super.onPause()
        "GankFragment onPause".logd()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        "GankFragment onDestroyView".logd()
    }

    override fun onStart() {
        super.onStart()
        "GankFragment onStart".logd()
    }
}
