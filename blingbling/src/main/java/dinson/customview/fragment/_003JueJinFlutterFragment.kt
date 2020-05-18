package dinson.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dinson.customview.R
import dinson.customview._global.BaseFragment
import dinson.customview.activity.CommonWebActivity
import dinson.customview.adapter._003JueJinArticleAdapter
import dinson.customview.api.GankApi
import dinson.customview.entity.gank.JueJinResponse
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.loge
import dinson.customview.weight.recycleview.LinearItemDecoration
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import dinson.customview.weight.refreshview.CustomRefreshView
import kotlinx.android.synthetic.main.fragment_003_juejin_flutter.*

/**
 * 掘金flutter专题
 */
class _003JueJinFlutterFragment : ViewPagerLazyFragment() {

    companion object {
        const val EXTRA_TAGS = "tags"
        fun newInstance(tagId: String): _003JueJinFlutterFragment {
            val args = Bundle()
            args.putString(EXTRA_TAGS, tagId)
            val fragment = _003JueJinFlutterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_003_juejin_flutter, container, false)
    }

    private var mTagId: String = ""

    private var mAdapter: _003JueJinArticleAdapter? = null
    private val mData = ArrayList<JueJinResponse.DBean.EntrylistBean>()
    private var mCurrentPage = 0
    private val mApi by lazy {
        HttpHelper.create(GankApi::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTagId = arguments?.getString(EXTRA_TAGS) ?: ""
        mAdapter = _003JueJinArticleAdapter(mData)
        crfFlutterContent.setAdapter(mAdapter)
        crfFlutterContent.setEmptyView("")
    }


    override fun lazyInit() {
        crfFlutterContent.recyclerView.addItemDecoration(LinearItemDecoration(context))
        crfFlutterContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer(true)
            }

            override fun onLoadMore() {
                getDataFromServer(false)
            }
        })
        crfFlutterContent.isRefreshing = true

        RvItemClickSupport.addTo(crfFlutterContent.recyclerView)
            .setOnItemClickListener(OnRvItemClickListener { _, view, position ->
                CommonWebActivity.start(view.context, mData[position].originalUrl)
            })
    }

    private fun getDataFromServer(isRefresh: Boolean) {
        if (isRefresh) mCurrentPage = 0

        mApi.loadJueJinArticle(mTagId, mCurrentPage)
            .compose(RxSchedulers.io_main())
            .subscribe({
                crfFlutterContent.complete()
                if (isRefresh) mData.clear()

                if (it.d == null || it.d.total == 0 || it.d.entrylist == null ||
                    it.d.entrylist.isEmpty())
                    crfFlutterContent.onNoMore()
                if (it.d != null && it.d.entrylist != null && it.d.entrylist.isNotEmpty())
                    mData.addAll(it.d.entrylist)
                mAdapter?.notifyDataSetChanged()
                mCurrentPage++
            }, {
                it.message?.loge()
                crfFlutterContent.complete()
            }).addToManaged()
    }
}
