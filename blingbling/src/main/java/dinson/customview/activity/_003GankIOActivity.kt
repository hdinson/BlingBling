package dinson.customview.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.trello.rxlifecycle2.android.ActivityEvent
import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.api.GankApi
import dinson.customview.entity.gank.ProjectsInfo
import dinson.customview.entity.gank.ProjectsTitle
import dinson.customview.holder.gankio.GankIOTypeFactory
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.click
import dinson.customview.kotlin.loge
import dinson.customview.kotlin.logi
import dinson.customview.kotlin.toast
import dinson.customview.weight.recycleview.OnRvItemClickListener
import dinson.customview.weight.recycleview.RvItemClickSupport
import dinson.customview.weight.recycleview.multitype.MultiType
import dinson.customview.weight.recycleview.multitype.MultiTypeAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity__003_gank_io.*
import java.util.*

class _003GankIOActivity : BaseActivity() {

    private val mApi by lazy { HttpHelper.create(GankApi::class.java) }
    private val mDataList = ArrayList<MultiType>()
    private lateinit var mAdapter: MultiTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__003_gank_io)

        initUI()
        refreshContent()
    }

    private fun initUI() {
        mAdapter = MultiTypeAdapter(mDataList, GankIOTypeFactory())
        rvContent.adapter = mAdapter
        rvContent.layoutManager = LinearLayoutManager(this)

        fabLady.click { _003GankLadyActivity.start(this) }
        RvItemClickSupport.addTo(rvContent).setOnItemClickListener(
            OnRvItemClickListener { _, _, position ->
                val bean = mDataList[position]
                if (bean is ProjectsInfo) {
                    CommonWebActivity.start(this, bean.url)
                }
            })
    }

    private fun refreshContent() {
        mApi.loadTodayData()
            .filter { it.isError.not() && it.results != null && it.results.isNotEmpty() }
            .map { bean ->
                mDataList.clear()
                bean.results.forEach each@{
                    if (it.key == "福利" && it.value.isNotEmpty()) {
                        initHeadPicture(it.value[0].url)
                        return@each
                    }
                    mDataList.add(ProjectsTitle(it.key))
                    mDataList.addAll(it.value)
                }
            }
            .compose(RxSchedulers.io_main())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({
                mAdapter.notifyDataSetChanged()
            }, {
                it.message?.toast()
                it.message?.loge()
            }).addToManaged()
    }

    private fun initHeadPicture(url: String) {
        url.logi()
        Observable.just(url)
            .map {
                Glide.with(this).asBitmap().load(url)
                    .submit(SIZE_ORIGINAL, SIZE_ORIGINAL).get()
            }
            .compose(RxSchedulers.io_main())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({
                "Pic load success !".logi()
                ivPicture.setImageBitmap(it)
            }, { it.message?.loge() }).addToManaged()
    }
}
