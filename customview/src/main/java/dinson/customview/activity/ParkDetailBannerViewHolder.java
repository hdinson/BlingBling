package dinson.customview.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import dinson.customview.utils.GlideUtils;
import dinson.customview.weight.banner.holder.MZViewHolder;

/**
 * @author Dinson - 2017/9/28
 */
public class ParkDetailBannerViewHolder implements MZViewHolder<String> {

    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        mImageView = new ImageView(context);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return mImageView;
    }

    @Override
    public void onBind(Context context, int position, String data) {

        GlideUtils.setImage(context,data,mImageView);
       /* Glide.with(context).load(data).placeholder(R.mipmap.bg_default_park)
            .error(R.mipmap.bg_default_park).into(mImageView);*/
    }
}
