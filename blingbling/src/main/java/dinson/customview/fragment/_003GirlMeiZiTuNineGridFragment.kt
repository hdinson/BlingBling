package dinson.customview.fragment

import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import dinson.customview.R
import dinson.customview._global.BaseFragment
import dinson.customview.adapter._003MeiZiTuNineGridAdapter
import dinson.customview.api.GankApi
import dinson.customview.entity.gank.MeiZiTuNineGrid
import dinson.customview.entity.gank.MeiZiTuNinePic
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.loge
import com.dinson.blingbase.kotlin.toasty
import dinson.customview.manager.GlideSimpleLoader

import dinson.customview.weight.MessagePicturesLayout
import dinson.customview.weight._003weight.DecorationLayout
import dinson.customview.weight.imagewatcher.ImageWatcherHelper
import dinson.customview.weight.refreshview.CustomRefreshView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_003_girl_nine_grid.*


/**
 * 妹子图9图样式界面
 */
abstract class _003GirlMeiZiTuNineGridFragment : ViewPagerLazyFragment(), MessagePicturesLayout.Callback {


    var mCurrentPage = 1

    private var mAdapter: _003MeiZiTuNineGridAdapter? = null
    private val mData = ArrayList<MeiZiTuNineGrid>()
    val mApi: GankApi by lazy { HttpHelper.create(GankApi::class.java) }
    private var mYearTag = ""


    override fun onCreateView(original: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return original.inflate(R.layout.fragment_003_girl_nine_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = _003MeiZiTuNineGridAdapter(mData)
        crfGirlsContent.setAdapter(mAdapter)
        crfGirlsContent.setEmptyView("")
        mAdapter!!.setPictureClickCallback(this)
    }

    override fun lazyInit() {
        crfGirlsContent.recyclerView.layoutManager = LinearLayoutManager(context)

        crfGirlsContent.setOnLoadListener(object : CustomRefreshView.OnLoadListener {
            override fun onRefresh() {
                getDataFromServer(true)
            }

            override fun onLoadMore() {
                getDataFromServer(false)
            }
        })
        crfGirlsContent.isRefreshing = true
    }

    private fun getDataFromServer(isRefresh: Boolean) {
        if (isRefresh) {
            mYearTag = ""
            mCurrentPage = 1
        }
        getObservable()
            .map {
                val gird = ArrayList<MeiZiTuNineGrid>()
                var tempTime = ""
                var child = MeiZiTuNineGrid()

                it.forEach { bean ->
                    val tTime = bean.date.split("T").first()
                    if (tTime.isNotEmpty() && tTime != tempTime) {
                        tempTime = tTime
                        child = MeiZiTuNineGrid()
                        child.time = tempTime
                        val split = tTime.split("-")
                        if (split[0] != mYearTag) {
                            mYearTag = split[0]
                            child.isShowYear = true
                        }
                        child.pictureThumbList.add(Uri.parse(bean.thumb_src))
                        child.pictureList.add(Uri.parse(bean.img_src))
                        gird.add(child)
                    } else if (tTime == tempTime) {
                        child.pictureThumbList.add(Uri.parse(bean.thumb_src))
                        child.pictureList.add(Uri.parse(bean.img_src))
                    }
                }
                gird
            }
            .compose(RxSchedulers.io_main())
            .subscribe({
                crfGirlsContent.complete()
                if (isRefresh) mData.clear()

                if (it.isEmpty())
                    crfGirlsContent.onNoMore()

                mData.addAll(it)
                mAdapter?.notifyDataSetChanged()
                mCurrentPage++

            }, {
                crfGirlsContent.complete()
                it.toString().toasty()
                loge { it.toString() }
            }).addToManaged()
    }

    private val layDecoration by lazy { DecorationLayout(context) }

    private val mPicHelper by lazy {
        ImageWatcherHelper.with(this.activity, GlideSimpleLoader())
            .setOtherView(layDecoration)
            .addOnPageChangeListener(layDecoration)
    }

    override fun onThumbPictureClick(i: ImageView?, imageGroupList: SparseArray<ImageView>?, urlList: MutableList<Uri>?) {
        layDecoration.attachImageWatcher(mPicHelper)
        mPicHelper.show(i, imageGroupList, urlList)
    }

    override fun onResume() {
        super.onResume()

        view?.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return@setOnKeyListener mPicHelper.handleBackPressed()
                }
                return@setOnKeyListener false
            }
        }
    }

    abstract fun getObservable(): Observable<ArrayList<MeiZiTuNinePic>>

}
