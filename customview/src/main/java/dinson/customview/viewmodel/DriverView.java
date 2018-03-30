package dinson.customview.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import dinson.customview.databinding.Item005ViewDriverFeedBinding;
import dinson.customview.model._005FileInfo;


/**
 * Created by wrh on 16/2/15.
 */
public class DriverView extends RelativeLayout {
    private _005FileInfo mResult;
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

    public void setData(final _005FileInfo data) {
        mResult = data;
        mBinding.setFileInfo(data);
        mViewTarget = new DriverViewTarget(mBinding.viewImgFeed);
        if (!mResult.isNull()) {
            setCardViewLayoutParams(mResult.getWidth(), mResult.getHeight());
        }

        String host = mResult.getDomain().startsWith("http") ? mResult.getDomain() + File.separator
                : "http://" + mResult.getDomain() + File.separator;

        this.post(() -> Glide.with(getContext()).asBitmap().load(host + data.getKey())
                .apply(new RequestOptions().centerCrop()
                        .override(BitmapImageViewTarget.SIZE_ORIGINAL, BitmapImageViewTarget.SIZE_ORIGINAL))
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
            if (mResult.isNull()) {
                int viewWidth = mBinding.viewImgFeed.getWidth();
                float scale = resource.getWidth() / (viewWidth * 1.0f);
                int viewHeight = (int) (resource.getHeight() / scale);
                setCardViewLayoutParams(viewWidth, viewHeight);
                mResult.setSize(viewWidth, viewHeight);
            }
            super.onResourceReady(resource, transition);
        }
    }
}
