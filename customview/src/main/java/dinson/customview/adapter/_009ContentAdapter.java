package dinson.customview.adapter;

import android.content.Context;

import java.util.List;

import dinson.customview.R;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * @author Dinson - 2017/7/21
 */
public class _009ContentAdapter extends CommonAdapter<_009PanoramaImageModel> {

    public _009ContentAdapter(Context context, List<_009PanoramaImageModel> dataList) {
        super(context, dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_009_panorana_content;
    }

    @Override
    public void convert(CommonViewHolder holder, _009PanoramaImageModel bean, int position) {
        holder.setTvText(R.id.tv_title, bean.title);
        holder.setTvText(R.id.tv_desc, bean.desc);
        GlideUtils.setImage(mContext, bean.url, holder.getView(R.id.iv_img));
    }
}
