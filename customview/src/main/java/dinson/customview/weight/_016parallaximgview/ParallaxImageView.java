package dinson.customview.weight._016parallaximgview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import dinson.customview.R;
import dinson.customview.utils.LogUtils;

/**
 * Created by gjz on 19/12/2016.
 */

public class ParallaxImageView extends AppCompatImageView {

    // Image's scroll orientation
    public final static byte ORIENTATION_NONE = -1;
    public final static byte ORIENTATION_HORIZONTAL = 0;
    public final static byte ORIENTATION_VERTICAL = 1;
    private byte mOrientation = ORIENTATION_NONE;

    // Enable panorama effect or not
    private boolean mEnablePanoramaMode;

    // If true, the image scroll left(top) when the device clockwise rotate along y-axis(x-axis).
    private boolean mInvertScrollDirection;

    // Image's width and height
    private int mDrawableWidth;
    private int mDrawableHeight;

    // View's width and height
    private int mWidth;
    private int mHeight;

    // Image's offset from initial state(center in the view).
    private float mMaxOffset;

    // The scroll progress.
    private float mProgress;

    // Show scroll bar or not
    private boolean mEnableScrollbar;

    // The paint to draw scrollbar
    private Paint mScrollbarPaint;

    // Observe scroll state
    private OnPanoramaScrollListener mOnPanoramaScrollListener;


    private Paint mContextPaint;
    private Bitmap mBmp1;
    private Bitmap mBmp2;
    private Bitmap mBmp3;
    private Bitmap mBmp4;
    private Bitmap mBmp5;
    private Bitmap mBmp6;
    private float mLeft;

    public ParallaxImageView(Context context) {
        this(context, null);
    }

    public ParallaxImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(ScaleType.CENTER_CROP);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxImageView);
        mEnablePanoramaMode = typedArray.getBoolean(R.styleable.ParallaxImageView_enableParallaxMode, true);
        mInvertScrollDirection = typedArray.getBoolean(R.styleable.ParallaxImageView_invertScrollDirection, false);
        mEnableScrollbar = typedArray.getBoolean(R.styleable.ParallaxImageView_showScrollbar, true);
        typedArray.recycle();

        if (mEnableScrollbar) {
            initScrollbarPaint();
        }


        mContextPaint = new Paint();
        mContextPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
    }

    private void initScrollbarPaint() {
        mScrollbarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScrollbarPaint.setColor(Color.WHITE);
        mScrollbarPaint.setStrokeWidth(dp2px(1.5f));
    }

    public void setGyroscopeObserver(GyroscopeObserver observer) {
        if (observer != null) {
            observer.addPanoramaImageView(this);
        }
    }

    void updateProgress(float progress) {
        if (mEnablePanoramaMode) {
            mProgress = mInvertScrollDirection ? -progress : progress;
            invalidate();
            if (mOnPanoramaScrollListener != null) {
                mOnPanoramaScrollListener.onScrolled(this, -mProgress);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        mHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        if (getDrawable() != null) {
            mDrawableWidth = getDrawable().getIntrinsicWidth();
            mDrawableHeight = getDrawable().getIntrinsicHeight();

            if (mDrawableWidth * mHeight > mDrawableHeight * mWidth) {
                mOrientation = ORIENTATION_HORIZONTAL;
                float imgScale = (float) mHeight / (float) mDrawableHeight;
                mMaxOffset = Math.abs((mDrawableWidth * imgScale - mWidth) * 0.5f);
            } else if (mDrawableWidth * mHeight < mDrawableHeight * mWidth) {
                mOrientation = ORIENTATION_VERTICAL;
                float imgScale = (float) mWidth / (float) mDrawableWidth;
                mMaxOffset = Math.abs((mDrawableHeight * imgScale - mHeight) * 0.5f);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        Matrix mMatrix = new Matrix();
        // 缩放原图
        float drawableHeight = (float) h / mDrawableHeight;
        mMatrix.postScale(drawableHeight, drawableHeight);

        mLeft = ((float)h/mDrawableHeight*mDrawableWidth-w)/2;


        Resources res = getResources();
        Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.a1);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.a2);
        Bitmap bmp3 = BitmapFactory.decodeResource(res, R.drawable.a3);
        Bitmap bmp4 = BitmapFactory.decodeResource(res, R.drawable.a4);
        Bitmap bmp5 = BitmapFactory.decodeResource(res, R.drawable.a5);
        Bitmap bmp6 = BitmapFactory.decodeResource(res, R.drawable.a6);

        mBmp1 = Bitmap.createBitmap(bmp1, 0, 0, bmp1.getWidth(), bmp1.getHeight(), mMatrix, true);
        mBmp2 = Bitmap.createBitmap(bmp2, 0, 0, bmp2.getWidth(), bmp2.getHeight(), mMatrix, true);
        mBmp3 = Bitmap.createBitmap(bmp3, 0, 0, bmp3.getWidth(), bmp3.getHeight(), mMatrix, true);
        mBmp4 = Bitmap.createBitmap(bmp4, 0, 0, bmp4.getWidth(), bmp4.getHeight(), mMatrix, true);
        mBmp5 = Bitmap.createBitmap(bmp5, 0, 0, bmp5.getWidth(), bmp5.getHeight(), mMatrix, true);
        mBmp6 = Bitmap.createBitmap(bmp6, 0, 0, bmp6.getWidth(), bmp6.getHeight(), mMatrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mEnablePanoramaMode || getDrawable() == null || isInEditMode()) {
            super.onDraw(canvas);
            return;
        }

        // Draw image
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            float currentOffsetX = mMaxOffset * mProgress;
            canvas.save();
            canvas.translate(currentOffsetX, 0);
            LogUtils.e("-----------"+currentOffsetX);
            super.onDraw(canvas);
            canvas.save();
            canvas.drawBitmap(mBmp1, -mLeft, 0, mContextPaint);
            canvas.translate(currentOffsetX - 5, 0);
            canvas.drawBitmap(mBmp2, -mLeft, 0, mContextPaint);
            canvas.translate(currentOffsetX - 10, 0);
            canvas.drawBitmap(mBmp3, -mLeft, 0, mContextPaint);
            canvas.translate(currentOffsetX - 15, 0);
            canvas.drawBitmap(mBmp4, -mLeft, 0, mContextPaint);
            canvas.translate(currentOffsetX - 20, 0);
            canvas.drawBitmap(mBmp5, -mLeft, 0, mContextPaint);
            canvas.translate(currentOffsetX - 25, 0);
            canvas.restore();
        } else if (mOrientation == ORIENTATION_VERTICAL) {
            float currentOffsetY = mMaxOffset * mProgress;
            canvas.save();
            canvas.translate(0, currentOffsetY);
            super.onDraw(canvas);
            canvas.restore();
        }

        // Draw scrollbar
        if (mEnableScrollbar) {
            switch (mOrientation) {
                case ORIENTATION_HORIZONTAL: {
                    float barBgWidth = mWidth * 0.9f;
                    float barWidth = barBgWidth * mWidth / mDrawableWidth;

                    float barBgStartX = (mWidth - barBgWidth) / 2;
                    float barBgEndX = barBgStartX + barBgWidth;
                    float barStartX = barBgStartX + (barBgWidth - barWidth) / 2 * (1 - mProgress);
                    float barEndX = barStartX + barWidth;
                    float barY = mHeight * 0.95f;

                    mScrollbarPaint.setAlpha(100);
                    canvas.drawLine(barBgStartX, barY, barBgEndX, barY, mScrollbarPaint);
                    mScrollbarPaint.setAlpha(255);
                    canvas.drawLine(barStartX, barY, barEndX, barY, mScrollbarPaint);
                    break;
                }
                case ORIENTATION_VERTICAL: {
                    float barBgHeight = mHeight * 0.9f;
                    float barHeight = barBgHeight * mHeight / mDrawableHeight;

                    float barBgStartY = (mHeight - barBgHeight) / 2;
                    float barBgEndY = barBgStartY + barBgHeight;
                    float barStartY = barBgStartY + (barBgHeight - barHeight) / 2 * (1 - mProgress);
                    float barEndY = barStartY + barHeight;
                    float barX = mWidth * 0.95f;

                    mScrollbarPaint.setAlpha(100);
                    canvas.drawLine(barX, barBgStartY, barX, barBgEndY, mScrollbarPaint);
                    mScrollbarPaint.setAlpha(255);
                    canvas.drawLine(barX, barStartY, barX, barEndY, mScrollbarPaint);
                    break;
                }
            }
        }
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public void setEnablePanoramaMode(boolean enable) {
        mEnablePanoramaMode = enable;
    }

    public boolean isPanoramaModeEnabled() {
        return mEnablePanoramaMode;
    }

    public void setInvertScrollDirection(boolean invert) {
        if (mInvertScrollDirection != invert) {
            mInvertScrollDirection = invert;
        }
    }

    public boolean isInvertScrollDirection() {
        return mInvertScrollDirection;
    }

    public void setEnableScrollbar(boolean enable) {
        if (mEnableScrollbar != enable) {
            mEnableScrollbar = enable;
            if (mEnableScrollbar) {
                initScrollbarPaint();
            } else {
                mScrollbarPaint = null;
            }
        }
    }

    public boolean isScrollbarEnabled() {
        return mEnableScrollbar;
    }

    public byte getOrientation() {
        return mOrientation;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        /**
         * Do nothing because PanoramaImageView only
         * supports {@link scaleType.CENTER_CROP}
         */
    }

    /**
     * Interface definition for a callback to be invoked when the image is scrolling
     */
    public interface OnPanoramaScrollListener {
        /**
         * Call when the image is scrolling
         *
         * @param view           the panoramaImageView shows the image
         * @param offsetProgress value between (-1, 1) indicating the offset progress.
         *                       -1 means the image scrolls to show its left(top) bound,
         *                       1 means the image scrolls to show its right(bottom) bound.
         */
        void onScrolled(ParallaxImageView view, float offsetProgress);
    }

    public void setOnPanoramaScrollListener(OnPanoramaScrollListener listener) {
        mOnPanoramaScrollListener = listener;
    }
}
