package dinson.customview.adapter

import android.content.Context
import dinson.customview.R
import dinson.customview.db.model.ZhihuTucao
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * 知乎列表适配器
 */
class _002ZhihuListAdapter(context: Context, dataList: List<ZhihuTucao>)
    : CommonAdapter<ZhihuTucao>(context, dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_002_zhihu_main

    override fun convert(holder: CommonViewHolder, dataBean: ZhihuTucao, position: Int) {
        holder.setTvText(R.id.tvTitle, dataBean.title)
        holder.setTvText(R.id.tvTime, dataBean.display_date)
        GlideUtils.setImage(mContext, dataBean.images, holder.getView(R.id.ivImg))
    }
}
