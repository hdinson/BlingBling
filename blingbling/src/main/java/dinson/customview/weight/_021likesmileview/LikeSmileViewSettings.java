package dinson.customview.weight._021likesmileview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dinson.customview.R;

/**
 * 点赞笑脸Settings
 */
class LikeSmileViewSettings {

    private final float mPercentSize;
    private final float mDescSize;

    private final int mIconSize;
    private final int mIconMargin;
    private final boolean mShowDivider;


    LikeSmileViewSettings(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LikeSmileView, 0, 0);
        mPercentSize = ta.getDimension(R.styleable.LikeSmileView_percentSize, sp2px(context, 18f));
        mDescSize = ta.getDimension(R.styleable.LikeSmileView_descSize, sp2px(context, 12f));
        mIconSize = (int) ta.getDimension(R.styleable.LikeSmileView_iconSize, dip2px(context, 30));
        mIconMargin = (int) ta.getDimension(R.styleable.LikeSmileView_iconMargin, dip2px(context, 30));

        mShowDivider = ta.getBoolean(R.styleable.LikeSmileView_showDivider, false);

        ta.recycle();
    }

    float getPercentSize() {
        return mPercentSize;
    }

    float getDescSize() {
        return mDescSize;
    }

    private static float sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    int getIconSize() {
        return mIconSize;
    }

    int getIconMargin() {
        return mIconMargin;
    }

    boolean isShowDivider() {
        return mShowDivider;
    }

    private static int dip2px(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
}
