package dinson.customview.adapter

import android.content.Context
import android.widget.TextView
import dinson.customview.R
import dinson.customview.model._012VideoBean
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * 视频数据适配器
 */
class _012VideoListAdapter(context: Context,
                           dataList: List<_012VideoBean>)
    : CommonAdapter<_012VideoBean>(context, dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_012_video

    override fun convert(holder: CommonViewHolder, video: _012VideoBean, position: Int) {
        holder.getView<TextView>(R.id.tvTitle).text = video.title
    }
}