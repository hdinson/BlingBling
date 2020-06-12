package com.dinson.blingbase.widget.banner;

import android.content.Context;
import android.util.AttributeSet;


/**
 * 广告轮播图setting
 */
public class BannerViewSettings {

    private final int mIndicatorMargin;
    private final int mIndicatorSize;
    private final int mIndicatorMarginBottom;
    private boolean mIsOpenMZEffect;
    private final boolean mIsMiddlePageCover;
    private boolean mIsCanLoop;
    private boolean mIsAutoLoop;
    private final boolean mHiddenIndicator;
    private final float mAspectRatio;
    private final int mIndicatorAlign;

    BannerViewSettings(Context context, AttributeSet attrs) {
        mIsOpenMZEffect =  true ;
        mAspectRatio = 2 ;
        mIsMiddlePageCover =  true ;
        mIsCanLoop =  true ;
        //当不支持轮播时，也不能自动轮播
        mIsAutoLoop = mIsCanLoop;
        mHiddenIndicator =  false;
        mIndicatorAlign =   1;
        mIndicatorMargin =   20;
        mIndicatorSize =  20;
        mIndicatorMarginBottom =   0;
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

    public int getIndicatorSize() {
        return mIndicatorSize;
    }
}
