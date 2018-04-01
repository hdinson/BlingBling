package dinson.customview.weight._013stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import dinson.customview.R;

/**
 * 广告轮播图setting
 */
public class StepViewSettings {

    private int mIndicatorPaddingLeftAndRight;      //指示器左右的padding
    private float mIndicatorHeight;                 //指示器的高
    private float mIndicatorWidth;                  //指示器的宽
    private float mIndicatorLineWidth;              //指示器的线宽
    private boolean mIsReverseDraw;                 //是否倒序
    private float mDashLineIntervals;               //虚线的虚实间隔
    private int mIndicatorCompletedLineColor;       //指示器已完成的线的颜色
    private int mIndicatorUnCompletedLineColor;     //指示器未完成的线的颜色
    private Drawable mIndicatorCompleteIcon;        //指示器已完成的图标
    private Drawable mIndicatorDefaultIcon;         //指示器未完成的图标
    private Drawable mIndicatorAttentionIcon;       //指示器正在进行的图标
    private boolean mIsVertical;                    //指示器的方向是否为垂直

    StepViewSettings(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.VerticalStepView, 0, 0);

        mIndicatorCompletedLineColor = attr.getColor(R.styleable.VerticalStepView_IndicatorCompletedLineColor, getColor(context, R.color.common_divider));
        mIndicatorUnCompletedLineColor = attr.getColor(R.styleable.VerticalStepView_IndicatorUnCompletedLineColor, getColor(context, R.color.common_divider));
        mIndicatorCompleteIcon = attr.getDrawable(R.styleable.VerticalStepView_IndicatorCompleteIcon);
        mIndicatorDefaultIcon = attr.getDrawable(R.styleable.VerticalStepView_IndicatorDefaultIcon);
        mIndicatorAttentionIcon = attr.getDrawable(R.styleable.VerticalStepView_IndicatorAttentionIcon);
        mDashLineIntervals = attr.getDimension(R.styleable.VerticalStepView_LinePaddingProportion, dip2px(context, 16));
        mIsReverseDraw = attr.getBoolean(R.styleable.VerticalStepView_ReverseDraw, false);
        mIsVertical = attr.getInt(R.styleable.VerticalStepView_IndicatorOrientation, 1) == 1;
        mIndicatorHeight = attr.getDimension(R.styleable.VerticalStepView_IndicatorHeight, Float.MAX_VALUE);
        mIndicatorWidth = attr.getDimension(R.styleable.VerticalStepView_IndicatorWidth, Float.MAX_VALUE);
        mIndicatorLineWidth = attr.getDimension(R.styleable.VerticalStepView_IndicatorLineWidth, dip2px(context, 1));
        mIndicatorPaddingLeftAndRight = (int) attr.getDimension(R.styleable.VerticalStepView_IndicatorPaddingLeftAndRight, dip2px(context, 16));

        attr.recycle();
    }

    public Drawable getIndicatorIcon(State state) {
        switch (state) {
            case Completed:
                return mIndicatorCompleteIcon;
            case Completing:
                return mIndicatorAttentionIcon;
            default:
                return mIndicatorDefaultIcon;
        }
    }

    private int dip2px(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    private int getColor(Context context, int res) {
        return ContextCompat.getColor(context, res);
    }

    public int getIndicatorCompletedLineColor() {
        return mIndicatorCompletedLineColor;
    }

    public void setIndicatorCompletedLineColor(int indicatorCompletedLineColor) {
        mIndicatorCompletedLineColor = indicatorCompletedLineColor;
    }

    public int getIndicatorUnCompletedLineColor() {
        return mIndicatorUnCompletedLineColor;
    }

    public void setIndicatorUnCompletedLineColor(int indicatorUnCompletedLineColor) {
        mIndicatorUnCompletedLineColor = indicatorUnCompletedLineColor;
    }

    public int getIndicatorPaddingLeftAndRight() {
        return mIndicatorPaddingLeftAndRight;
    }

    public void setIndicatorPaddingLeftAndRight(int indicatorPaddingLeftAndRight) {
        mIndicatorPaddingLeftAndRight = indicatorPaddingLeftAndRight;
    }

    public void setIndicatorCompleteIcon(Drawable indicatorCompleteIcon) {
        mIndicatorCompleteIcon = indicatorCompleteIcon;
    }

    public void setIndicatorDefaultIcon(Drawable indicatorDefaultIcon) {
        mIndicatorDefaultIcon = indicatorDefaultIcon;
    }

    public void setIndicatorAttentionIcon(Drawable indicatorAttentionIcon) {
        mIndicatorAttentionIcon = indicatorAttentionIcon;
    }

    public boolean isVertical() {
        return mIsVertical;
    }

    public void setVertical(boolean vertical) {
        mIsVertical = vertical;
    }

    public float getDashLineIntervals() {
        return mDashLineIntervals;
    }

    public void setDashLineIntervals(float dashLineIntervals) {
        mDashLineIntervals = dashLineIntervals;
    }

    public boolean isReverseDraw() {
        return mIsReverseDraw;
    }

    public void setReverseDraw(boolean reverseDraw) {
        mIsReverseDraw = reverseDraw;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setIndicatorHeight(float indicatorHeight) {
        mIndicatorHeight = indicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public void setIndicatorWidth(float indicatorWidth) {
        mIndicatorWidth = indicatorWidth;
    }

    public float getIndicatorLineWidth() {
        return mIndicatorLineWidth;
    }

    public void setIndicatorLineWidth(float indicatorLineWidth) {
        mIndicatorLineWidth = indicatorLineWidth;
    }
}
