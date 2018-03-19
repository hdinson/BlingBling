package dinson.customview.adapter

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.api.WanAndroidApi
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.http.HttpHelper
import dinson.customview.http.RxSchedulers
import dinson.customview.kotlin.*
import dinson.customview.utils.DateUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import java.util.concurrent.TimeUnit

/**
 *玩安卓列表适配器
 */
class _001WanAndroidMainListAdapter(context: Context, dataList: List<WanAndArticle>)
    : CommonAdapter<WanAndArticle>(context, dataList) {

    private val mWanAndroidApi = HttpHelper.create(WanAndroidApi::class.java)

    override fun getLayoutId(viewType: Int) = R.layout.item_001_wan_android_main

    override fun convert(holder: CommonViewHolder, dataBean: WanAndArticle, position: Int) {
        holder.setTvText(R.id.tvTitle, dataBean.title)
        holder.setTvText(R.id.tvChapter, "${dataBean.superChapterName}/${dataBean.chapterName}")
        val tag = holder.getView<TextView>(R.id.tvTag)
        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            tag.show()
            tag.text = dataBean.tags[0].name
        } else {
            tag.hide(true)
        }
        holder.setTvText(R.id.tvTime, DateUtils.long2Str(dataBean.publishTime, "MM/dd HH:mm"))
        val likeView = holder.getView<CheckBox>(R.id.likeView)
        likeView.isChecked = dataBean.isCollect

        RxView.clicks(likeView).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                verbose(likeView.isChecked then "添加收藏" ?: "取消收藏")
                post2Server(likeView.isChecked, dataBean)
            }
    }

    private fun post2Server(checked: Boolean, dataBean: WanAndArticle) {
        val observable = checked then mWanAndroidApi.addCollect(dataBean.id)
            ?: mWanAndroidApi.delCollectFromMainList(dataBean.id)
        observable.compose(RxSchedulers.io_main())
            .subscribe({

            }, {
                error(it.toString())
            })
    }
}
