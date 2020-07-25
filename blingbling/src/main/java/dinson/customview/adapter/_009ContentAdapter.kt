package dinson.customview.adapter

import android.view.View
import dinson.customview.R
import dinson.customview.kotlin.logi
import dinson.customview.model._009PanoramaImageModel
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_009_panorana_content.view.*

/**
 * googleVR列表数据适配器
 */
class _009ContentAdapter(dataList: MutableList<_009PanoramaImageModel>)
    : CommonAdapter<_009PanoramaImageModel>(dataList) {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_009_panorana_content


    override fun convert(holder: CommonViewHolder, bean: _009PanoramaImageModel, position: Int) {
        holder.itemView.tvTitle.text = bean.title
        holder.itemView.tvDesc.text = bean.desc
        GlideUtils.setImage(holder.itemView.context, bean.smallImg, holder.itemView.ivImg)

        val current = (bean.progress * 100).toInt()

        logi { "current: $current" }

        holder.itemView.progress.visibility = if (current == 0 || current == 100) View.INVISIBLE else View.VISIBLE
        holder.itemView.complete.visibility = if (current == 100) View.VISIBLE else View.INVISIBLE
        holder.itemView.progress.setProgress(current)
    }
}
