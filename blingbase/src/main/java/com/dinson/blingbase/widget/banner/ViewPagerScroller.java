package com.dinson.blingbase.widget.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 滑动的控件（主要用于控制viewpager 的速度）
 */
@SuppressWarnings("unused")
public class ViewPagerScroller extends Scroller {
    private int mDuration = 800;// ViewPager默认的最大Duration 为600,我们默认稍微大一点。值越大越慢。
    private boolean mIsUseDefaultDuration = false;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mIsUseDefaultDuration ? duration : mDuration);
    }

    public void setUseDefaultDuration(boolean useDefaultDuration) {
        mIsUseDefaultDuration = useDefaultDuration;
    }

    public boolean isUseDefaultDuration() {
        return mIsUseDefaultDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }


    public int getScrollDuration() {
        return mDuration;
    }
}
