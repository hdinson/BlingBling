package dinson.customview.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import dinson.customview.R;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.weight.recycleview.CommonAdapter;
import dinson.customview.weight.recycleview.CommonViewHolder;

/**
 * 主页适配器
 */
public class MainHeadAdapter extends CommonAdapter<DailyDetail> {

    private final RequestOptions mOptions;
    private final DrawableTransitionOptions mTransitionOptions;

    public MainHeadAdapter(Context context, List<DailyDetail> dataList) {
        super(context, dataList);
        mOptions = new RequestOptions()
            .error(R.drawable.def_img).diskCacheStrategy(DiskCacheStrategy.DATA);
        mTransitionOptions = new DrawableTransitionOptions().crossFade(500);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_main_head;
    }

    @Override
    public void convert(CommonViewHolder holder, DailyDetail bean, int position) {
        Glide.with(mContext).load(bean.getData().getHp_img_url()).transition(mTransitionOptions)
            .apply(mOptions).into((ImageView) holder.getView(R.id.ivImg));
    }
}
