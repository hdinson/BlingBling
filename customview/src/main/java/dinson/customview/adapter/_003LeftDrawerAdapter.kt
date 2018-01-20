package dinson.customview.adapter

import android.content.Context
import android.widget.ImageView

import dinson.customview.R
import dinson.customview.model._003CurrencyModel
import dinson.customview.utils.GlideUtils
import dinson.customview.weight.recycleview.CommonAdapter
import dinson.customview.weight.recycleview.CommonViewHolder

/**
 * @author Dinson - 2017/7/21
 */
class _003LeftDrawerAdapter(context: Context,
                            dataList: List<_003CurrencyModel>)
    : CommonAdapter<_003CurrencyModel>(context, dataList) {

    override fun getLayoutId(viewType: Int) = R.layout.item_003_left_drawer

    override fun convert(holder: CommonViewHolder, currencyModel: _003CurrencyModel, position: Int) {
        holder.setTvText(R.id.tvTitle, currencyModel.currencyCn)
        holder.setTvText(R.id.tvDesc, currencyModel.currencyCode)
        GlideUtils.setImage(mContext, currencyModel.imgUrl, holder.getView(R.id.ivImg))
    }
}
