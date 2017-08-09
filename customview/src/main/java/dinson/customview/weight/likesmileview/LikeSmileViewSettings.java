package dinson.customview.weight.likesmileview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dinson.customview.R;
import dinson.customview.utils.UIUtils;

/**
 * @author Dinson - 2017/8/9
 */
public class LikeSmileViewSettings {

    private final float mPercentSize;
    private final float mDescSize;

    private final int mIconSize;
    private final int mIconMargin;

    LikeSmileViewSettings(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LikeSmileView, 0, 0);
        mPercentSize = ta.getDimension(R.styleable.LikeSmileView_percentSize, sp2px(context, 20f));
        mDescSize = ta.getDimension(R.styleable.LikeSmileView_descSize, sp2px(context, 14f));
        mIconSize = (int) ta.getDimension(R.styleable.LikeSmileView_iconSize, UIUtils.dip2px(25));
        mIconMargin = (int) ta.getDimension(R.styleable.LikeSmileView_iconMargin, UIUtils.dip2px(25));


        ta.recycle();
    }

    public float getPercentSize() {
        return mPercentSize;
    }

    public float getDescSize() {
        return mDescSize;
    }

    public static float sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    public int getIconSize() {
        return mIconSize;
    }

    public int getIconMargin() {
        return mIconMargin;
    }
}
