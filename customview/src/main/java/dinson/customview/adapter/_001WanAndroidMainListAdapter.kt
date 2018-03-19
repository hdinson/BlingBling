package dinson.customview.adapter

import android.content.Context
import android.widget.TextView
import dinson.customview.R
import dinson.customview.entity.wanandroid.WanAndArticle
import dinson.customview.kotlin.hide
import dinson.customview.kotlin.show
import dinson.customview.utils.DateUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 *玩安卓列表适配器
 */
class _001WanAndroidMainListAdapter(context: Context, dataList: List<WanAndArticle>)
    : CommonAdapter<WanAndArticle>(context, dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_001_wan_android_main

    override fun convert(holder: CommonViewHolder, dataBean: WanAndArticle, position: Int) {
        holder.setTvText(R.id.tvTitle, dataBean.title)
        holder.setTvText(R.id.tvChapter, "${dataBean.superChapterName}/${dataBean.chapterName}")
        val tag = holder.getView<TextView>(R.id.tvTag)
        if (dataBean.tags != null && dataBean.tags.isNotEmpty()) {
            tag.show()
            tag.text = "${dataBean.tags[0]}"
        } else {
            tag.hide(true)
        }
        holder.setTvText(R.id.tvTime, "time: ${DateUtils.long2Str(dataBean.publishTime, "MM-dd HH:mm")}")
    }
}
