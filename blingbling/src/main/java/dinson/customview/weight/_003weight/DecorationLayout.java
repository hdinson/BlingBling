package dinson.customview.weight._003weight;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import dinson.customview.R;
import dinson.customview.utils.LogUtils;
import dinson.customview.weight.imagewatcher.ImageWatcher;
import dinson.customview.weight.imagewatcher.ImageWatcherHelper;


public class DecorationLayout extends FrameLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ImageWatcherHelper mHolder;
    private int currentPosition;
    private int mPagerPositionOffsetPixels;

    public DecorationLayout(Context context) {
        this(context, null);
    }

    public DecorationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final FrameLayout vContainer = (FrameLayout) LayoutInflater.from(context)
            .inflate(R.layout.layout_003_download_pic, this);
        View vDownload = vContainer.findViewById(R.id.ivDownload);
        vDownload.setOnClickListener(this);
    }

    public void attachImageWatcher(ImageWatcherHelper iwHelper) {
        mHolder = iwHelper;
    }

    @Override
    public void onClick(View v) {
        if (mPagerPositionOffsetPixels != 0) return;
        if (v.getId() == R.id.ivDownload) {
            Uri uri = mHolder.getImageWatcher().getUri(currentPosition);
            LogUtils.e("click: " + uri.toString());
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        mPagerPositionOffsetPixels = i1;
        notifyAlphaChanged(v);
    }

    @Override
    public void onPageSelected(int i) {
        currentPosition = i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void notifyAdapterItemChanged(int position, Uri newUri) {
        if (mHolder != null) {
            final ImageWatcher iw = mHolder.getImageWatcher();
            if (iw != null) {
                iw.notifyItemChanged(position, newUri);
            }
        }
    }

    /**
     * 滑动透明
     */
    private void notifyAlphaChanged(float p) {
        if (0 < p && p <= 0.2f) {
            float r = (0.2f - p) * 5;
            setAlpha(r);
        } else if (0.8f <= p && p < 1) {
            float r = (p - 0.8f) * 5;
            setAlpha(r);
        } else if (p == 0) {
            setAlpha(1f);
        } else {
            setAlpha(0f);
        }
    }
}
