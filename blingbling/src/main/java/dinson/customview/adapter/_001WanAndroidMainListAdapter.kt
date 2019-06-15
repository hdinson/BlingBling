package dinson.customview.adapter

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.activity.CommonWebActivity
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.kotlin.click
import dinson.customview.kotlin.hide
import dinson.customview.kotlin.show
import dinson.customview.kotlin.logv
import dinson.customview.listener._001OnLikeViewClickListener
import dinson.customview.utils.DateUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_001_wan_android_main.view.*
import java.util.concurrent.TimeUnit

/**
 *玩安卓列表适配器
 */
class _001WanAndroidMainListAdapter(
    dataList: List<WanAndArticle>,
    private val likeClickListener: _001OnLikeViewClickListener)
    : CommonAdapter<WanAndArticle>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_001_wan_android_main

    override fun convert(holder: CommonViewHolder, dataBean: WanAndArticle, position: Int) {
        holder.itemView.tvTitle.text = dataBean.title
        holder.itemView.tvChapter.text = "${dataBean.superChapterName}/${dataBean.chapterName}"

        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            holder.itemView.tvTag.show()
            holder.itemView.tvTag.text = dataBean.tags[0].name
        } else {
            holder.itemView.tvTag.hide(true)
        }
        holder.itemView.tvTime.text = DateUtils.long2Str(dataBean.publishTime, "MM/dd")

        holder.itemView.likeView.isChecked = dataBean.isCollect

        RxView.clicks(holder.itemView.likeView).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                logv(if (holder.itemView.likeView.isChecked) "添加收藏" else "取消收藏")
                //post2Server(likeView.isChecked, dataBean)
                likeClickListener.onClickLikeView(holder.itemView.likeView, dataBean, position)
            }

        holder.itemView.click {
            CommonWebActivity.start(it.context, dataBean.link)
        }
    }
}