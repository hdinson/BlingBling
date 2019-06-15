package dinson.customview.adapter

import dinson.customview.BuildConfig
import dinson.customview.R
import dinson.customview.model._007CurrencyModel
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder
import kotlinx.android.synthetic.main.item_007_left_drawer.view.*

/**
 * 选择货币列表适配器
 */
class _007LeftDrawerAdapter(dataList: List<_007CurrencyModel>)
    : CommonAdapter<_007CurrencyModel>(dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_007_left_drawer

    override fun convert(holder: CommonViewHolder, currencyModel: _007CurrencyModel, position: Int) {
        holder.itemView.tvTitle.text = currencyModel.currencyCn
        holder.itemView.tvDesc.text = currencyModel.currencyCode
        GlideUtils.setImage(holder.itemView.context, "${BuildConfig.QINIU_URL}${currencyModel.imgUrl}",
            holder.itemView.ivImg)
    }
}
