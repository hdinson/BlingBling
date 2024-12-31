package dinson.customview.widget._014honorclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import dinson.customview.R;

/**
 * 仿华为时钟setting
 */
public class HonorClockViewSettings {

    private int mViewWidth;
    private float mRingWidth;
    private int mPointColor;
    private int mTimeTextColor;
    private final int mTimeScaleColor;
    private final float mTimeTextSize;

    HonorClockViewSettings(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.HonorClockView, 0, 0);

        mViewWidth = dip2px(context, 220);
        mRingWidth = attr.getDimension(R.styleable.HonorClockView_RingWidth, dip2px(context, 10));
        mPointColor = attr.getColor(R.styleable.HonorClockView_PointColor, Color.RED);
        mTimeScaleColor = attr.getColor(R.styleable.HonorClockView_TimeScaleColor, Color.parseColor("#bbbbbb"));
        mTimeTextColor = attr.getColor(R.styleable.HonorClockView_TimeTextColor, Color.parseColor("#dddddd"));
        mTimeTextSize = attr.getDimension(R.styleable.HonorClockView_TimeTextSize, dip2px(context,50));

        attr.recycle();
    }

    private int dip2px(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public int getViewWidth() {
        return mViewWidth;
    }

    public void setViewWidth(int viewWidth) {
        mViewWidth = viewWidth;
    }

    public float getRingWidth() {
        return mRingWidth;
    }

    public void setRingWidth(float ringWidth) {
        mRingWidth = ringWidth;
    }

    public int getPointColor() {
        return mPointColor;
    }

    public void setPointColor(int pointColor) {
        mPointColor = pointColor;
    }

    public int getTimeTextColor() {
        return mTimeTextColor;
    }

    public void setTimeTextColor(int timeTextColor) {
        mTimeTextColor = timeTextColor;
    }

    public float getTimeTextSize() {
        return mTimeTextSize;
    }

    public int getTimeScaleColor() {
        return mTimeScaleColor;
    }
}
