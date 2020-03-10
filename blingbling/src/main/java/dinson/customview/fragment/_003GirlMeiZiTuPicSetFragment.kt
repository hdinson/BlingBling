package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import dinson.customview.R
import dinson.customview._global.BaseFragment
import dinson.customview.activity._003PicSetListActivity
import dinson.customview.adapter._003MeiZiTuPicSetAdapter
import dinson.customview.api.GankApi
import dinson.customview.entity.gank.MeiZiTuPicSet
import dinson.customview.http.HttpHelper
import dinson.customview.kotlin.dip
import dinson.customview.kotlin.logd
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.toast
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_003_girl_pic_set.*

/**
 * 妹子图图集样式界面
 */
abstract class _003GirlMeiZiTuPicSetFragment : BaseFragment() {

    var mCurrentPage = 1
    val mApi: GankApi by lazy { HttpHelper.create(GankApi::class.java) }

    private var mAdapterTopSet: _003MeiZiTuPicSetAdapter? = null
    private val mData = ArrayList<MeiZiTuPicSet>()


    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_003_girl_pic_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapterTopSet = _003MeiZiTuPicSetAdapter(mData)
        crfGirlsContent.setAdapter(mAdapterTopSet)
        crfGirlsContent.setEmptyView("")
    }

    override fun onResumeLazyLoad(): Boolean {
        crfGirlsContent.recyclerView.layoutManager = GridLayoutManager(context, 2)
        crfGirlsContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer(true)
            }

            override fun onLoadMore() {
                getDataFromServer(false)
            }
        })
        crfGirlsContent.isRefreshing = true
        crfGirlsContent.loadMoreEnable = needLoadMore()
        crfGirlsContent.recyclerView.setPadding(dip(4), 0, dip(4), 0)
        crfGirlsContent.recyclerView.isVerticalScrollBarEnabled = false

        RvItemClickSupport.addTo(crfGirlsContent.recyclerView)
            .setOnItemClickListener(OnRvItemClickListener { _, _, position ->
                if (context != null) {
                    _003PicSetListActivity.start(context!!, mData[position].id, mData[position].title)
                }
            })
        return true
    }

    private fun getDataFromServer(isRefresh: Boolean) {
        if (isRefresh && needIncrement()) mCurrentPage = 1
        getObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                crfGirlsContent.complete()
                if (isRefresh) mData.clear()

                if (it.isEmpty())
                    crfGirlsContent.onNoMore()

                mData.addAll(it)
                mAdapterTopSet?.notifyDataSetChanged()
                mCurrentPage++
            }, {
                crfGirlsContent.complete()
                it.toString().toast()
                it.toString().loge()
            }).addToManaged()
    }

    abstract fun getObservable(): Observable<ArrayList<MeiZiTuPicSet>>

    open fun needIncrement() = true
    open fun needLoadMore() = true

}
