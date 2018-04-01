package dinson.customview.weight.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dinson.customview.R;


/**
 * 广告轮播图setting
 */
public class BannerViewSettings {

    private final int mIndicatorMargin;
    private final int mIndicatorMarginBottom;
    private boolean mIsOpenMZEffect;
    private final boolean mIsMiddlePageCover;
    private boolean mIsCanLoop;
    private boolean mIsAutoLoop;
    private final boolean mHiddenIndicator;
    private final float mAspectRatio;
    private final int mIndicatorAlign;

    BannerViewSettings(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);
        mIsOpenMZEffect = attr.getBoolean(R.styleable.BannerView_open_mz_mode, true);
        mAspectRatio = attr.getFloat(R.styleable.BannerView_bannerAspectRatio, 2);
        mIsMiddlePageCover = attr.getBoolean(R.styleable.BannerView_middle_page_cover, true);
        mIsCanLoop = attr.getBoolean(R.styleable.BannerView_canLoop, true);
        //当不支持轮播时，也不能自动轮播
        mIsAutoLoop = mIsCanLoop && attr.getBoolean(R.styleable.BannerView_autoLoop, true);
        mHiddenIndicator = attr.getBoolean(R.styleable.BannerView_hiddenIndicator, false);
        mIndicatorAlign = attr.getInt(R.styleable.BannerView_indicatorAlign, 1);
        mIndicatorMargin = attr.getDimensionPixelSize(R.styleable.BannerView_indicatorMargin, 0);
        mIndicatorMarginBottom = attr.getDimensionPixelSize(R.styleable.BannerView_indicatorMarginBottom, 0);
        attr.recycle();
    }

    public int getIndicatorMargin() {
        return mIndicatorMargin / 2;
    }

    public int getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public boolean isOpenMZEffect() {
        return mIsOpenMZEffect;
    }

    public void setOpenMZEffect(boolean openMZEffect) {
        mIsOpenMZEffect = openMZEffect;
    }

    public boolean isMiddlePageCover() {
        return mIsMiddlePageCover;
    }

    public boolean isCanLoop() {
        return mIsCanLoop;
    }

    public void setCanLoop(boolean canLoop) {
        mIsCanLoop = canLoop;
    }

    public int getIndicatorAlign() {
        return mIndicatorAlign;
    }

    public boolean isAutoLoop() {
        return mIsCanLoop && mIsAutoLoop;
    }

    public void setAutoLoop(boolean autoLoop) {
        mIsAutoLoop = autoLoop;
    }

    public boolean isHiddenIndicator() {
        return mHiddenIndicator;
    }

    public float aspectRatio() {
        return mAspectRatio;
    }
}
