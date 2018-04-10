package dinson.customview.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dinson.customview.R
import dinson.customview.model._009PanoramaImageModel
import dinson.customview.model._015AppIcon
import dinson.customview.utils.GlideUtils
import dinson.customview.utils.LogUtils
import dinson.customview.weight.TasksCompletedView
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * App图标适配器
 */
class _015AppIconAdapter(context: Context, dataList: List<_015AppIcon>) :
    CommonAdapter<_015AppIcon>(context, dataList) {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_015_app_icon
    }


    override fun convert(holder: CommonViewHolder, bean: _015AppIcon, position: Int) {
        holder.getView<TextView>(R.id.tvName).text = bean.name
        holder.getView<ImageView>(R.id.ivImg).setImageResource(bean.img)
    }
}