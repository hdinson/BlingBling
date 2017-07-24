package dinson.customview.adapter;

import android.content.Context;

import java.util.List;

import dinson.customview.R;
import dinson.customview.entity.MonsterHunter.DataBean;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * @author Dinson - 2017/7/21
 */
public class _004LeftAdapter extends CommonAdapter<DataBean> {


    public _004LeftAdapter(Context context, List<DataBean> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_004_left;
    }

    @Override
    public void convert(CommonViewHolder holder, DataBean dataBean, int position) {
        holder.setTvText(R.id.tv_name, dataBean.getFamily());
    }
}
