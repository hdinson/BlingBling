package dinson.customview.weight.banner.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.LogUtils;

/**
 * @author Dinson - 2017/9/28
 */
public class TestBannerViewHolder implements BannerViewHolder<String> {

    private ImageView mImageView;

    @Override
    public View createView(Context context) {

        LogUtils.e("创建视图");


        mImageView = new ImageView(context);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return mImageView;
    }

    @Override
    public void onBind(Context context, int position, String data ) {

        LogUtils.e("position:"+position+"    设置图片");

        GlideUtils.setImage(context,data,mImageView);
    }

}
