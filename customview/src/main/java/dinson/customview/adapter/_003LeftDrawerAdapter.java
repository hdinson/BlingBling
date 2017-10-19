package dinson.customview.adapter;

import android.content.Context;

import java.util.List;

import dinson.customview.R;
import dinson.customview.model._003CurrencyModel;
import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * @author Dinson - 2017/7/21
 */
public class _003LeftDrawerAdapter extends CommonAdapter<_003CurrencyModel> {


    public _003LeftDrawerAdapter(Context context, List<_003CurrencyModel> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_003_left_drawer;
    }

    @Override
    public void convert(CommonViewHolder holder, _003CurrencyModel currencyModel, int position) {
        holder.setTvText(R.id.tvTitle, currencyModel.getCurrencyCn());
        holder.setTvText(R.id.tvDesc, currencyModel.getCurrencyCode());
        GlideUtils.setImage(mContext, currencyModel.getImgUrl(), holder.getView(R.id.ivImg));

    }
}
