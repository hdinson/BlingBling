package dinson.customview.adapter

import android.content.Context
import android.view.View
import dinson.customview.R
import dinson.customview.kotlin.info
import dinson.customview.model._009PanoramaImageModel
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.TasksCompletedView
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * googleVR列表数据适配器
 */
class _009ContentAdapter(context: Context, dataList: List<_009PanoramaImageModel>)
    : CommonAdapter<_009PanoramaImageModel>(context, dataList) {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_009_panorana_content


    override fun convert(holder: CommonViewHolder, bean: _009PanoramaImageModel, position: Int) {
        holder.setTvText(R.id.tvTitle, bean.title)
        holder.setTvText(R.id.tvDesc, bean.desc)
        GlideUtils.setImage(mContext, bean.smallImg, holder.getView(R.id.ivImg))

        val progress = holder.getView<TasksCompletedView>(R.id.progress)
        val current = (bean.progress * 100).toInt()

        info("current: $current")

        progress.visibility = if (current == 0 || current == 100) View.INVISIBLE else View.VISIBLE
        holder.getView<View>(R.id.complete).visibility = if (current == 100) View.VISIBLE else View.INVISIBLE
        progress.setProgress(current)
    }
}
