package dinson.customview.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import dinson.customview.R;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.LogUtils;
import dinson.customview.weight.TasksCompletedView;
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
        holder.setTvText(R.id.tvTitle, bean.title);
        holder.setTvText(R.id.tvDesc, bean.desc);
        GlideUtils.setImage(mContext, bean.smallImg, holder.getView(R.id.ivImg));

        TasksCompletedView progress = holder.getView(R.id.progress);
        int current = (int) (bean.getProgress() * 100);

        LogUtils.e("current: " + current);

        progress.setVisibility((current == 0 || current == 100) ? View.INVISIBLE : View.VISIBLE);
        holder.getView(R.id.complete).setVisibility(current == 100 ? View.VISIBLE : View.INVISIBLE);
        progress.setProgress(current);
    }
}
