package dinson.customview.adapter

import dinson.customview.R
import dinson.customview.db.model.ZhihuTucao
import dinson.customview.utils.GlideUtils
import com.dinson.blingbase.widget.recycleview.CommonAdapter
import com.dinson.blingbase.widget.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_002_zhihu_main.view.*

/**
 * 知乎列表适配器
 */
class _002ZhihuListAdapter(dataList: MutableList<ZhihuTucao>)
    : CommonAdapter<ZhihuTucao>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_002_zhihu_main

    override fun convert(holder: CommonViewHolder, dataBean: ZhihuTucao, position: Int) {
        holder.itemView.tvTitle.text = dataBean.title
        holder.itemView.tvTime.text = dataBean.display_date
        GlideUtils.setImage(holder.itemView.context, dataBean.images, holder.itemView.ivImg)
    }
}
