package dinson.customview.adapter;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import dinson.customview.R;
import dinson.customview.listener.OnItemSwipeOpen;
import dinson.customview.model._003CurrencyModel;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.LogUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;
import dinson.customview.weight.swipelayout.SwipeItemLayout;

/**
 * @author Dinson - 2017/7/21
 */
public class _003CurrencyAdapter extends CommonAdapter<_003CurrencyModel> {


    private OnItemSwipeOpen mListener;

    public _003CurrencyAdapter(Context context, List<_003CurrencyModel> dataList, OnItemSwipeOpen listener) {
        super(context, dataList);
        mListener = listener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_003_currency;
    }

    @Override
    public void convert(CommonViewHolder holder, _003CurrencyModel dataBean, int position) {
        LogUtils.e("_003: adapter : convert : called : position : " + position);
        GlideUtils.setImage(mContext, dataBean.getImgUrl(), holder.getView(R.id.ivImg));
        holder.setTvText(R.id.tvCurrencyCode, dataBean.getCurrencyCode());
        holder.setTvText(R.id.tvCurrencyCn, String.format(Locale.CHINA, "%s %s", dataBean.getCurrencyCn(), dataBean.getSign()));

        SwipeItemLayout delete = holder.getView(R.id.deleteLayout);
        delete.setSwipeEnable(true);
        delete.addSwipeListener((view, isOpen) -> {
            if (isOpen) mListener.onOpen(view,position);
        });
    }
}
