package dinson.customview.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.*
import dinson.customview.R
import dinson.customview.activity._027MovieDetailsActivity
import dinson.customview.adapter._027HomeAdapter
import dinson.customview.entity.av.Movie
import dinson.customview.kotlin.logi
import dinson.customview.model._027AvModel
import com.dinson.blingbase.widget.recycleview.OnRvItemClickListener
import com.dinson.blingbase.widget.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_027_home.*
import java.util.*


/**
 * 首页界面
 */
class _027HomeFragment : ViewPagerLazyFragment() {


    private var mPage = 1
    private val mDataList = ArrayList<Movie>()
    private var mAdapter: _027HomeAdapter? = null

    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_027_home, container, false)
    }

    override fun lazyInit() {
        mAdapter = _027HomeAdapter(mDataList)
        crfHomeContent.setAdapter(mAdapter)
        crfHomeContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getServiceData(true)
            }

            override fun onLoadMore() {
                getServiceData(false)
            }
        })
        crfHomeContent.isRefreshing = true
        crfHomeContent.setEmptyView("")
        crfHomeContent.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            RvItemClickSupport.addTo(this).setOnItemClickListener(OnRvItemClickListener { _, _, pos ->
                if (pos >= 0 && pos < mDataList.size) {
                    _027MovieDetailsActivity.start(context, mDataList[pos])
                }
            })
        }

    }

    private fun getServiceData(isRefresh: Boolean) {
        mPage = if (isRefresh) 1 else mPage + 1
        val url = "${_027AvModel.HOME}$mPage"
        logi { url }
        Observable.just(url)
            .map { _027AvModel.decodeMovie(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (isRefresh) {
                    mDataList.clear()
                }
                if (it.isEmpty() || it.size < 10) crfHomeContent.onNoMore()
                else crfHomeContent.onLoadingMore()

                mDataList.addAll(it)
                mAdapter?.notifyDataSetChanged()
                crfHomeContent.complete()
            }.addToManaged()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu._001_main_toolbar, menu)
    }

}
