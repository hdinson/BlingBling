package dinson.customview.adapter

import com.dinson.blingbase.widget.DrawableTextView
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import dinson.customview.R
import dinson.customview.entity._006HomeItem
import kotlinx.android.synthetic.main.item_006_home_item.view.*


/**
 * 识别模块
 */
class _006HomeItemAdapter(dataList: MutableList<_006HomeItem>)
    : CommonAdapter<_006HomeItem>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_006_home_item

    override fun convert(holder: CommonViewHolder, bean: _006HomeItem, position: Int) {
        holder.itemView.isByLocal.isEnabled = bean.isByLocal
        holder.itemView.tvTitle.text = bean.title
        holder.itemView.tvTitle.setDrawable(DrawableTextView.TOP, bean.drawableTop)
    }
}
