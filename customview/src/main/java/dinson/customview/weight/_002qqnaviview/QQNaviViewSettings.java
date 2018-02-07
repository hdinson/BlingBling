package dinson.customview.weight._002qqnaviview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dinson.customview.R;


/**
 * QQ导航setting
 */
public class QQNaviViewSettings {

    /* 外层icon资源 */
    private final int mBigIconSrc, mBigIconSrcCheck;
    /* 里面icon资源 */
    private final int mSmallIconSrc, mSmallIconSrcCheck;
    /* icon高度宽度 */
    private final float mIconWidth, mIconHeight;
    /* 拖动范围 可调 */
    private final float mRange;
    /* 底部的文字 */
    private final String mBottomText;
    /*文字和图片的距离*/
    private final float mTextPadding;
    /*底部文字大小*/
    private final float mBottomTextSize;

        /*
        <attr name="bottomTextColor" format="reference"/>
        <attr name="bottomTextSelectorColor" format="reference"/>
        <attr name="bottomTextSize" format="dimension"/>
        */

    QQNaviViewSettings(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QQNaviView, defStyleAttr, 0);
        mBigIconSrc = ta.getResourceId(R.styleable.QQNaviView_bigIconSrc, R.drawable._002_bubble_big);
        mBigIconSrcCheck = ta.getResourceId(R.styleable.QQNaviView_bigIconSrcCheck, R.drawable._002_pre_bubble_big);
        mSmallIconSrc = ta.getResourceId(R.styleable.QQNaviView_smallIconSrc, 0);
        mSmallIconSrcCheck = ta.getResourceId(R.styleable.QQNaviView_smallIconSrcCheck, 0);
        mIconWidth = ta.getDimension(R.styleable.QQNaviView_iconWidth, dip2px(context, 40));
        mIconHeight = ta.getDimension(R.styleable.QQNaviView_iconHeight, dip2px(context, 40));
        mRange = ta.getFloat(R.styleable.QQNaviView_range, 1);
        mBottomText = ta.getString(R.styleable.QQNaviView_bottomText);
        mTextPadding = ta.getDimension(R.styleable.QQNaviView_textPadding, 0);
        mBottomTextSize = ta.getDimension(R.styleable.QQNaviView_bottomTextSize, dip2px(context,12));
        ta.recycle();
    }

    private int dip2px(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public int getBigIconSrc() {
        return mBigIconSrc;
    }

    public int getBigIconSrcCheck() {
        return mBigIconSrcCheck;
    }

    public int getSmallIconSrc() {
        return mSmallIconSrc;
    }

    public int getSmallIconSrcCheck() {
        return mSmallIconSrcCheck;
    }

    public float getIconWidth() {
        return mIconWidth;
    }

    public float getIconHeight() {
        return mIconHeight;
    }

    public float getRange() {
        return mRange;
    }


    public String getBottomText() {
        return mBottomText == null ? "" : mBottomText;
    }


    public float getTextPadding() {
        return mTextPadding;
    }

    public float getBottomTextSize() {
        return mBottomTextSize;
    }
}
