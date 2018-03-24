package dinson.customview.bindingview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;

import dinson.customview.databinding.Item005ViewDriverFeedBinding;
import dinson.customview.model.SizeModel;
import dinson.customview.model._005FileInfo;
import dinson.customview.utils.GlideUtils;


/**
 * Created by wrh on 16/2/15.
 */
public class DriverView extends RelativeLayout   {
    private _005FileInfo mResult;
    private SizeModel mSizeModel;
    private Item005ViewDriverFeedBinding mBinding;
    private DriverViewTarget mViewTarget;

    public DriverView(Context context) {
        super(context);
        initView(context);
    }

    public DriverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DriverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mBinding = Item005ViewDriverFeedBinding.inflate(inflater, this, true);
    }

    public void setData(final _005FileInfo data, SizeModel sizeModel) {
        mResult = data;
        mBinding.setFileInfo(data);
        mSizeModel = sizeModel;
        mViewTarget = new DriverViewTarget(mBinding.viewImgFeed);
        if (!mSizeModel.isNull()) {
            setCardViewLayoutParams(mSizeModel.getWidth(), mSizeModel.getHeight());
        }

        this.post(() -> Glide.with(getContext()).asBitmap().load("http://ondlsj2sn.bkt.clouddn.com/" + data.getKey())
                .apply(new RequestOptions().centerCrop()
                        .override(mBinding.viewImgFeed.getWidth(), BitmapImageViewTarget.SIZE_ORIGINAL))
                .into(mViewTarget));
    }

    private void setCardViewLayoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = mBinding.viewImgFeed.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mBinding.viewImgFeed.setLayoutParams(layoutParams);
    }

    /* @Override
     public void onClick(View v) {
         Intent intent = new Intent(getContext(), PhotoActivity.class);
         intent.putExtra(PhotoActivity.IMAGE_URL, mResult.getUrl());
         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
             ByteArrayOutputStream stream = new ByteArrayOutputStream();
             ((BitmapDrawable) mBinding.viewImgFeed.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
             intent.putExtra(PhotoActivity.BITMAP, stream.toByteArray());
             ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(), mBinding.viewImgFeed, "driverView");
             getContext().startActivity(intent, options.toBundle());
         } else {
             getContext().startActivity(intent);
         }
     }
 */
    private class DriverViewTarget extends BitmapImageViewTarget {

        public DriverViewTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            if (mSizeModel.isNull()) {
                int viewWidth = mBinding.viewImgFeed.getWidth();
                float scale = resource.getWidth() / (viewWidth * 1.0f);
                int viewHeight = (int) (resource.getHeight() * scale);
                setCardViewLayoutParams(viewWidth, viewHeight);
                mSizeModel.setSize(viewWidth, viewHeight);
            }
            super.onResourceReady(resource, transition);
        }
    }
}
