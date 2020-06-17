package dinson.customview.adapter

import dinson.customview.R
import dinson.customview.model._015AppIcon
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_015_app_icon.view.*

/**
 * App图标适配器
 */
class _015AppIconAdapter(dataList: List<_015AppIcon>) :
    CommonAdapter<_015AppIcon>(dataList) {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_015_app_icon
    }


    override fun convert(holder: CommonViewHolder, bean: _015AppIcon, position: Int) {
        holder.itemView.tvName.text = bean.name
        holder.itemView.ivImg.setImageResource(bean.img)
    }
}