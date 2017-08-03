package dinson.customview.adapter;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

import dinson.customview.R;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * 主页适配器
 */
public class MainHeadAdapter extends CommonAdapter<DailyDetail> {


    public MainHeadAdapter(Context context, List<DailyDetail> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_main_head;
    }

    @Override
    public void convert(CommonViewHolder holder, DailyDetail bean, int position) {
        GlideUtils.setImageCacheData(mContext, bean.getData().getHp_img_url(), (ImageView) holder.getView(R.id.iv_img));
    }
}
