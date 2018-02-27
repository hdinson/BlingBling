package dinson.customview.weight._016parallaximgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dinson.customview.R;

/**
 * 带有视差的图片层
 */
public class ParallaxImageViewSettings {


    private boolean mEnableParallaxMode;
    private boolean mInvertScrollDirection;
    private boolean mShowScrollbar;

    ParallaxImageViewSettings(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ParallaxImageView, 0, 0);

        mEnableParallaxMode = attr.getBoolean(R.styleable.ParallaxImageView_enableParallaxMode, true);
        mInvertScrollDirection = attr.getBoolean(R.styleable.ParallaxImageView_invertScrollDirection, false);
        mShowScrollbar = attr.getBoolean(R.styleable.ParallaxImageView_showScrollbar, true);

        attr.recycle();
    }

    public boolean isEnableParallaxMode() {
        return mEnableParallaxMode;
    }

    public void setEnableParallaxMode(boolean enableParallaxMode) {
        mEnableParallaxMode = enableParallaxMode;
    }

    public boolean isInvertScrollDirection() {
        return mInvertScrollDirection;
    }

    public void setInvertScrollDirection(boolean invertScrollDirection) {
        mInvertScrollDirection = invertScrollDirection;
    }

    public boolean isShowScrollbar() {
        return mShowScrollbar;
    }

    public void setShowScrollbar(boolean showScrollbar) {
        mShowScrollbar = showScrollbar;
    }
}
