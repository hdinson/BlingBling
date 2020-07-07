package dinson.customview.adapter

import com.jakewharton.rxbinding2.view.RxView
import dinson.customview.R
import dinson.customview.activity.CommonWebActivity
import dinson.customview.entity.wanandroid.WanAndArticle
import com.dinson.blingbase.kotlin.click
import com.dinson.blingbase.kotlin.hide
import com.dinson.blingbase.kotlin.logv
import com.dinson.blingbase.kotlin.show
import com.dinson.blingbase.utils.DateUtils
import dinson.customview.listener._001OnLikeViewClickListener
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_001_wan_android_main.view.*
import java.util.concurrent.TimeUnit

/**
 *玩安卓 "我的喜欢" 列表适配器
 */
class _001WanAndroidLikeListAdapter(dataList: List<WanAndArticle>,
                                    private val likeClickListener: _001OnLikeViewClickListener)
    : CommonAdapter<WanAndArticle>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_001_wan_android_main

    override fun convert(holder: CommonViewHolder, dataBean: WanAndArticle, position: Int) {
        holder.itemView.tvTitle.text = dataBean.title

        val chapterStr = if (dataBean.superChapterName == null) dataBean.chapterName
        else "${dataBean.superChapterName}/${dataBean.chapterName}"
        holder.itemView.tvChapter.text = chapterStr

        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            holder.itemView.tvTag.show()
            holder.itemView.tvTag.text = dataBean.tags[0].name
        } else {
            holder.itemView.tvTag.hide(true)
        }

        holder.itemView.tvTime.text = DateUtils.long2Str(dataBean.publishTime, "MM/dd")

        holder.itemView.likeView.isChecked = true
        RxView.clicks(holder.itemView.likeView).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                logv(if (holder.itemView.likeView.isChecked) "添加收藏" else "取消收藏")
                //post2Server(likeView, dataBean, position)
                likeClickListener.onClickLikeView(holder.itemView.likeView, dataBean, position)
            }

        holder.itemView.click { CommonWebActivity.start(it.context, dataBean.link) }
    }
}