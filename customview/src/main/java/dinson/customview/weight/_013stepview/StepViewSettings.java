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

    private boolean mIsReverseDraw;
    private float mLinePaddingProportion;
    private int mIndicatorCompletedLineColor;
    private int mIndicatorUnCompletedLineColor;
    private int mCompletedTextColor;
    private int mUnCompletedTextColor;
    private Drawable mIndicatorCompleteIcon;
    private Drawable mIndicatorDefaultIcon;
    private Drawable mIndicatorAttentionIcon;
    private boolean mIsVertical;

    StepViewSettings(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.VerticalStepView, 0, 0);

        mIndicatorCompletedLineColor = attr.getColor(R.styleable.VerticalStepView_IndicatorCompletedLineColor, getColor(context, R.color.common_divider));
        mIndicatorUnCompletedLineColor = attr.getColor(R.styleable.VerticalStepView_IndicatorUnCompletedLineColor, getColor(context, R.color.common_divider));
        mCompletedTextColor = attr.getColor(R.styleable.VerticalStepView_CompletedTextColor, getColor(context, R.color.textSecond));
        mUnCompletedTextColor = attr.getColor(R.styleable.VerticalStepView_UnCompletedTextColor, getColor(context, R.color.textHint));
        mIndicatorCompleteIcon = attr.getDrawable(R.styleable.VerticalStepView_IndicatorCompleteIcon);
        mIndicatorDefaultIcon = attr.getDrawable(R.styleable.VerticalStepView_IndicatorDefaultIcon);
        mIndicatorAttentionIcon = attr.getDrawable(R.styleable.VerticalStepView_IndicatorAttentionIcon);
        mLinePaddingProportion = attr.getFloat(R.styleable.VerticalStepView_LinePaddingProportion, 1);
        mIsReverseDraw = attr.getBoolean(R.styleable.VerticalStepView_ReverseDraw, true);
        mIsVertical = attr.getInt(R.styleable.VerticalStepView_IndicatorOrientation, 1) == 1;


        attr.recycle();
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

    public int getCompletedTextColor() {
        return mCompletedTextColor;
    }

    public void setCompletedTextColor(int complectedTextColor) {
        mCompletedTextColor = complectedTextColor;
    }

    public int getUnCompletedTextColor() {
        return mUnCompletedTextColor;
    }

    public void setUnCompletedTextColor(int unCompletedTextColor) {
        mUnCompletedTextColor = unCompletedTextColor;
    }

    public Drawable getIndicatorCompleteIcon() {
        return mIndicatorCompleteIcon;
    }

    public void setIndicatorCompleteIcon(Drawable indicatorCompleteIcon) {
        mIndicatorCompleteIcon = indicatorCompleteIcon;
    }

    public Drawable getIndicatorDefaultIcon() {
        return mIndicatorDefaultIcon;
    }

    public void setIndicatorDefaultIcon(Drawable indicatorDefaultIcon) {
        mIndicatorDefaultIcon = indicatorDefaultIcon;
    }

    public Drawable getIndicatorAttentionIcon() {
        return mIndicatorAttentionIcon;
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

    public void setLinePaddingProportion(float linePaddingProportion) {
        mLinePaddingProportion = linePaddingProportion;
    }

    public float getLinePaddingProportion() {
        return mLinePaddingProportion;
    }

    public boolean isReverseDraw() {
        return mIsReverseDraw;
    }

    public void setReverseDraw(boolean reverseDraw) {
        mIsReverseDraw = reverseDraw;
    }
}
