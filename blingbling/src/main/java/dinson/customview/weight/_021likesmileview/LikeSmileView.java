package dinson.customview.weight._021likesmileview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview.utils.LogUtils;
import dinson.customview.utils.TypefaceUtils;

/**
 * 点赞笑脸view
 */
public class LikeSmileView extends LinearLayout implements View.OnClickListener {
    String defLike = "喜欢", defDis = "无感";
    int defTextColor = Color.WHITE;
    final int mMaxHeight = 400, mMinHeight = 50;//单位px
    private ObjectAnimator mObjectAnimator;
    private ValueAnimator mValueAnimator;

    public void setDefDis(String defDis, String defLike) {
        mTvDisText.setText(defDis);
        mTvLikeText.setText(defLike);
    }

    public void onDestroy() {
        LogUtils.v("LikeSmileView: onDestroy() called");
        if (mValueAnimator != null) mValueAnimator.cancel();
        if (mObjectAnimator != null) mObjectAnimator.cancel();
        if (animatorBack != null) animatorBack.cancel();
        animLike.stop();
        animDis.stop();
    }


    public void setNum(int like, int dislike) {
        mLikeNum = like;
        mDisLikeNum = dislike;
        calculateLikeAndDis(like, dislike);
    }

    protected LikeSmileViewSettings mSettings;//自定义属性

    private int mLikeNum = 1, mDisLikeNum = 1;//原始点赞数,原始差评数
    private float mCurrentPercentLike, mCurrentPercentDis;//当前点赞数和差评数


    private ImageView mIvLike, mIvDis;//布局控件
    private TextView mTvLikeNum, mTvDisNum, mTvLikeText, mTvDisText;//布局控件
    private FrameLayout mFlLikeBg, mFlDisBg;//布局控件

    private AnimationDrawable animLike, animDis; //笑脸帧动画
    private ValueAnimator animatorBack; //背景拉伸动画

    private int type = 0; //选择执行帧动画的笑脸 //0 笑脸 1 哭脸
    private boolean isClose = false; //判断收起动画

    public LikeSmileView(Context context) {
        this(context, null);
    }

    public LikeSmileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeSmileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSettings = new LikeSmileViewSettings(context, attrs);

        init();
        bindListener();
    }

    private void init() {

        this.removeAllViews();
        //初始化总布局
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.argb(0xaa, 48, 48, 48)); //开始透明

        //初始化图片
        mIvLike = new ImageView(getContext());
        mIvLike.setScaleType(ImageView.ScaleType.FIT_XY);
        //添加动画资源  获得帧动画
        mIvLike.setBackgroundResource(R.drawable._021_animation_like);
        animLike = (AnimationDrawable) mIvLike.getBackground();
        mIvDis = new ImageView(getContext());
        mIvDis.setScaleType(ImageView.ScaleType.FIT_XY);
        mIvDis.setBackgroundResource(R.drawable._021_animation_dislike);
        animDis = (AnimationDrawable) mIvDis.getBackground();

        //初始化文字
        Typeface typeface = TypefaceUtils.INSTANCE.getAppleFont(getContext());
        mTvLikeNum = new TextView(getContext());
        mTvLikeNum.setTypeface(typeface);
        mTvLikeNum.setTextColor(defTextColor);
        TextPaint likeNumPaint = mTvLikeNum.getPaint();
        likeNumPaint.setFakeBoldText(true);
        mTvLikeNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getPercentSize());
        mTvLikeText = new TextView(getContext());
        mTvLikeText.setText(defLike);
        mTvLikeText.setTextColor(defTextColor);
        mTvLikeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getDescSize());

        mTvDisNum = new TextView(getContext());
        mTvDisNum.setTypeface(typeface);
        mTvDisNum.setTextColor(defTextColor);
        TextPaint disNumPaint = mTvDisNum.getPaint();
        disNumPaint.setFakeBoldText(true);
        mTvDisNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getPercentSize());
        mTvDisText = new TextView(getContext());
        mTvDisText.setText(defDis);
        mTvDisText.setTextColor(defTextColor);
        mTvDisText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getDescSize());

        //初始化布局
        mFlLikeBg = new FrameLayout(getContext());
        mFlDisBg = new FrameLayout(getContext());
        LayoutParams ivParams = new LayoutParams(mSettings.getIconSize(), mSettings.getIconSize());
        mFlLikeBg.addView(mIvLike, ivParams);
        mFlDisBg.addView(mIvDis, ivParams);
        mFlDisBg.setBackgroundResource(R.drawable._021_white_bg);
        mFlLikeBg.setBackgroundResource(R.drawable._021_white_bg);

        //单列总布局
        LinearLayout mLikeLayout = new LinearLayout(getContext());
        LinearLayout mDisLayout = new LinearLayout(getContext());
        mLikeLayout.setOrientation(VERTICAL);
        mDisLayout.setOrientation(VERTICAL);
        mLikeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mDisLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        //添加文字图片放进一列
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.setMargins(0, 10, 0, 0);
        params.gravity = Gravity.CENTER;
        mDisLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mLikeLayout.setGravity(Gravity.END);
        mDisLayout.addView(mTvDisNum, params);
        mDisLayout.addView(mTvDisText, params);
        mDisLayout.addView(mFlDisBg, params);

        mLikeLayout.addView(mTvLikeNum, params);
        mLikeLayout.addView(mTvLikeText, params);
        mLikeLayout.addView(mFlLikeBg, params);

        LayoutParams disLikeParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams LikeParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LikeParam.setMargins(0, 0, mSettings.getIconMargin(), 0);
        addView(mDisLayout, LikeParam);
        addView(mLikeLayout, disLikeParam);


        setTvAlpha(0);//隐藏文字
        calculateLikeAndDis(mLikeNum, mDisLikeNum);//设置百分比
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.e("onLayout load");
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 设置文字及背景的透明度
     *
     * @param alpha 透明度
     */
    public void setTvAlpha(float alpha) {
        mTvLikeNum.setAlpha(alpha);
        mTvDisNum.setAlpha(alpha);
        mTvLikeText.setAlpha(alpha);
        mTvDisText.setAlpha(alpha);
        getBackground().setAlpha((int) (alpha * 255));
    }

    /**
     * mMinHeight = 50
     */
    private void bindListener() {
        mIvDis.setId(R.id.tvDis);
        mIvLike.setId(R.id.tvLike);
        mIvDis.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
    }

    /**
     * 点赞和取消的点击事件
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        int tempLike = id == R.id.tvLike ? mLikeNum + 1 : mLikeNum;
        int tempDis = id == R.id.tvLike ? mDisLikeNum : mDisLikeNum + 1;
        calculateLikeAndDis(tempLike, tempDis);

        mFlLikeBg.setBackgroundResource(id == R.id.tvLike ? R.drawable._021_yellow_bg : R.drawable._021_white_bg);
        mFlDisBg.setBackgroundResource(id == R.id.tvLike ? R.drawable._021_white_bg : R.drawable._021_yellow_bg);

        switch (id) {
            case R.id.tvLike:
                type = 0;
                mIvDis.setBackground(null);
                mIvDis.setBackgroundResource(R.drawable._021_animation_dislike);
                animDis = (AnimationDrawable) mIvDis.getBackground();
                break;
            case R.id.tvDis:
                type = 1; //设置动画对象
                mIvLike.setBackground(null);
                mIvLike.setBackgroundResource(R.drawable._021_animation_like);
                animLike = (AnimationDrawable) mIvLike.getBackground();
                break;
        }
        animBack();//开始动画
    }

    /**
     * 背景伸展动画
     */
    public void animBack() {
        //动画执行中不能点击
        mIvDis.setClickable(false);
        mIvLike.setClickable(false);
        isClose = false;

        animatorBack = ValueAnimator.ofFloat(0, 1);
        animatorBack.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorBack.addUpdateListener(mAnimatorUpdateListener);
        animatorBack.addListener(mAnimatorListener);
        animatorBack.setDuration(100);
        animatorBack.start();
    }


    /**
     * 背景收回动画
     */
    public void setBackUp() {
        animatorBack.setInterpolator(new LinearInterpolator());
        animatorBack.setDuration(300);
        animatorBack.setFloatValues(1, 0);
        animatorBack.start();
    }


    public void objectY() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(0, 0);
            mValueAnimator.setRepeatMode(ObjectAnimator.RESTART);
            mValueAnimator.setDuration(1500);
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setBackUp(); //执行回弹动画
                }
            });
        }
        mValueAnimator.start();
    }

    public void objectX(View view) {
        if (mObjectAnimator == null) {
            mObjectAnimator = ObjectAnimator.ofFloat(view, "translationX", -10.0f, 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
            mObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
            mObjectAnimator.setDuration(2000);

            mObjectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setBackUp(); //执行回弹动画
                }
            });
        }
        mObjectAnimator.start();
    }

    private AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            setTvAlpha(value);

            FrameLayout.LayoutParams likeParams = (FrameLayout.LayoutParams) mIvLike.getLayoutParams();
            FrameLayout.LayoutParams disParams = (FrameLayout.LayoutParams) mIvDis.getLayoutParams();
            likeParams.bottomMargin = (int) ((mCurrentPercentLike * mMaxHeight + mMinHeight) * value);
            mIvLike.setLayoutParams(likeParams);
            disParams.bottomMargin = (int) ((mCurrentPercentDis * mMaxHeight + mMinHeight) * value);
            mIvDis.setLayoutParams(disParams);
        }
    };

    private Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //重置帧动画
            animDis.stop();
            animLike.stop();

            //关闭时不执行帧动画
            if (isClose) {
                //收回后可点击
                mIvDis.setClickable(true);
                mIvLike.setClickable(true);

                setTvAlpha(0);//隐藏文字
                return;
            }
            isClose = true;

            if (type == 0) {
                animLike.start();
                objectY();
            } else {
                animDis.start();
                objectX(mIvDis);
            }

        }
    };

    private SpannableString formatPercent(float value) {
        int percent = (int) (value * 100 + 0.5f);
        SpannableString sp = new SpannableString(percent + "%");
        sp.setSpan(new RelativeSizeSpan(0.7f), sp.length() - 1, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    private void calculateLikeAndDis(int like, int dis) {
        //设置百分比
        float count = like + dis;
        mCurrentPercentDis = dis / count;
        mCurrentPercentLike = 1 - mCurrentPercentDis;
        mTvLikeNum.setText(formatPercent(mCurrentPercentLike));
        mTvDisNum.setText(formatPercent(mCurrentPercentDis));
    }
}
