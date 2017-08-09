package dinson.customview.weight.likesmileview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dinson.customview.R;

/**
 * @author Dinson - 2017/8/9
 */
public class LikeSmileView extends LinearLayout implements Animator.AnimatorListener {
    protected LikeSmileViewSettings mSettings;//自定义属性


    private float count;
    //分割线间距
    private int dividerMargin = 20;


    public void notifyChange() {
        init();
        bindListener();
    }


    public void setDefalutGravity(int defalutGravity) {
        this.defalutGravity = defalutGravity;
    }

    public void setDefDis(String defDis) {
        this.defDis = defDis;
    }

    public void setDefLike(String defLike) {
        this.defLike = defLike;

    }

    public LikeSmileView setDividerMargin(int dividerMargin) {
        this.dividerMargin = dividerMargin;
        return this;
    }


    public void setNum(int like, int dislike) {
        //设置百分比
        count = like + dislike;
        float fLike = like / count;
        float fDis = dislike / count;
        this.mPercentLike = (int) (fLike * 100);
        this.mPercentDisLike = 100 - this.mPercentLike;
        setPercentLike(this.mPercentLike);
        setPercentDisLike(this.mPercentDisLike);
    }

    public void setPercentLike(int percentLike) {
        likeNum.setText(percentLike + "%");
    }

    public void setPercentDisLike(int percentDisLike) {
        disNum.setText(percentDisLike + "%");
    }

    private String defLike = "喜欢", defDis = "无感";
    private int defTextColor = Color.WHITE;

    private String defaluteShadow = "#484848";
    private int defalutGravity = Gravity.CENTER_HORIZONTAL;
    //private int defalutSize = dip2px(getContext(), 25);

    private int mPercentLike = 1;
    private int mPercentDisLike = 1; //点赞数,差评数
    //private float fLike, fDis;
    private ImageView imageLike;
    private ImageView imageDis;

    private TextView likeNum, disNum, likeText, disText;
    private LinearLayout likeBack, disBack, likeAll, disAll;
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
        setGravity(defalutGravity | Gravity.BOTTOM);
        setBackgroundColor(Color.TRANSPARENT); //开始透明

        //设置百分比
        float count = mPercentLike + mPercentDisLike;
        float fDis = mPercentDisLike / count;

        mPercentDisLike = (int) (fDis * 100);
        mPercentLike = 100 - mPercentDisLike;

        //初始化图片
        imageLike = new ImageView(getContext());
        //添加动画资源  获得帧动画
        imageLike.setBackgroundResource(R.drawable._005_animation_like);
        animLike = (AnimationDrawable) imageLike.getBackground();
        //初始化文字
        likeNum = new TextView(getContext());
        likeNum.setText(mPercentLike + "%");
        likeNum.setTextColor(defTextColor);
        TextPaint likeNumPaint = likeNum.getPaint();
        likeNumPaint.setFakeBoldText(true);
        likeNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getPercentSize());
        likeText = new TextView(getContext());
        likeText.setText(defLike);
        likeText.setTextColor(defTextColor);
        likeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getDescSize());

        imageDis = new ImageView(getContext());
        imageDis.setBackgroundResource(R.drawable._005_animation_dislike);
        animDis = (AnimationDrawable) imageDis.getBackground();
        disNum = new TextView(getContext());
        disNum.setText(mPercentDisLike + "%");
        disNum.setTextColor(defTextColor);
        TextPaint disNumPaint = disNum.getPaint();
        disNumPaint.setFakeBoldText(true);
        disNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getPercentSize());
        disText = new TextView(getContext());
        disText.setText(defDis);
        disText.setTextColor(defTextColor);
        disText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettings.getDescSize());


        //初始化布局
        likeBack = new LinearLayout(getContext());
        disBack = new LinearLayout(getContext());
        LayoutParams params2 = new LayoutParams(mSettings.getIconSize(), mSettings.getIconSize());
        likeBack.addView(imageLike, params2);
        disBack.addView(imageDis, params2);
        disBack.setBackgroundResource(R.drawable._005_white_bg);
        likeBack.setBackgroundResource(R.drawable._005_white_bg);

        //单列总布局
        likeAll = new LinearLayout(getContext());
        disAll = new LinearLayout(getContext());
        likeAll.setOrientation(VERTICAL);
        disAll.setOrientation(VERTICAL);
        likeAll.setGravity(Gravity.CENTER_HORIZONTAL);
        disAll.setGravity(Gravity.CENTER_HORIZONTAL);
        likeAll.setBackgroundColor(Color.TRANSPARENT);
        disAll.setBackgroundColor(Color.TRANSPARENT);

        //添加文字图片放进一列
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.setMargins(0, 10, 0, 0);
        params.gravity = Gravity.CENTER;
        disAll.setGravity(Gravity.CENTER_HORIZONTAL);
        likeAll.setGravity(Gravity.END);
        disAll.addView(disNum, params);
        disAll.addView(disText, params);
        disAll.addView(disBack, params);

        likeAll.addView(likeNum, params);
        likeAll.addView(likeText, params);
        likeAll.addView(likeBack, params);


        LayoutParams params3 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.gravity = Gravity.BOTTOM;
        addView(disAll, params3);
        int margin = mSettings.getIconMargin() / 2;
        params3.setMargins(margin, 0, margin, 0);
        addView(likeAll, params3);

        //隐藏文字
        setVisibities(GONE);
    }

    //
    public void setVisibities(int v) {
        likeNum.setVisibility(v);
        disNum.setVisibility(v);
        likeText.setVisibility(v);
        disText.setVisibility(v);
    }

    //绑定监听
    private void bindListener() {
        imageDis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1; //设置动画对象
                animBack(); //拉伸背景
                setVisibities(VISIBLE); //隐藏文字
                //切换背景色
                setBackgroundColor(Color.parseColor(defaluteShadow));
                getBackground().setAlpha(50);
                likeBack.setBackgroundResource(R.drawable._005_white_bg);
                disBack.setBackgroundResource(R.drawable._005_yellow_bg);
                //重置帧动画
                imageLike.setBackground(null);
                imageLike.setBackgroundResource(R.drawable._005_animation_like);
                animLike = (AnimationDrawable) imageLike.getBackground();
            }
        });
        imageLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                animBack();
                setVisibities(VISIBLE);
                setBackgroundColor(Color.parseColor(defaluteShadow));
                getBackground().setAlpha(50);
                disBack.setBackgroundResource(R.drawable._005_white_bg);
                likeBack.setBackgroundResource(R.drawable._005_yellow_bg);
                imageDis.setBackground(null);
                imageDis.setBackgroundResource(R.drawable._005_animation_dislike);
                animDis = (AnimationDrawable) imageDis.getBackground();
            }
        });
    }

    //背景伸展动画
    public void animBack() {
        //动画执行中不能点击
        imageDis.setClickable(false);
        imageLike.setClickable(false);

        final int max = Math.max(mPercentLike * 4, mPercentDisLike * 4);
        animatorBack = ValueAnimator.ofInt(5, max);
        animatorBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int magrin = (int) animation.getAnimatedValue();
                LayoutParams paramsLike
                    = (LayoutParams) imageLike.getLayoutParams();
                paramsLike.bottomMargin = magrin;

                if (magrin <= mPercentLike * 4) {
                    imageLike.setLayoutParams(paramsLike);
                }
                if (magrin <= mPercentDisLike * 4) {
                    imageDis.setLayoutParams(paramsLike);
                }
            }
        });
        isClose = false;
        animatorBack.addListener(this);
        animatorBack.setDuration(100);
        animatorBack.start();
    }

    //背景收回动画
    public void setBackUp() {
        final int max = Math.max(mPercentLike * 4, mPercentDisLike * 4);
        animatorBack = ValueAnimator.ofInt(max, 5);
        animatorBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int magrin = (int) animation.getAnimatedValue();
                LayoutParams paramsLike
                    = (LayoutParams) imageLike.getLayoutParams();
                paramsLike.bottomMargin = magrin;

                if (magrin <= mPercentLike * 4) {
                    imageLike.setLayoutParams(paramsLike);
                }
                if (magrin <= mPercentDisLike * 4) {
                    imageDis.setLayoutParams(paramsLike);
                }
            }
        });
        animatorBack.addListener(this);
        animatorBack.setDuration(500);
        animatorBack.start();
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //重置帧动画
        animDis.stop();
        animLike.stop();

        //关闭时不执行帧动画
        if (isClose) {
            //收回后可点击
            imageDis.setClickable(true);
            imageLike.setClickable(true);
            //隐藏文字
            setVisibities(GONE);
            //恢复透明
            setBackgroundColor(Color.TRANSPARENT);
            return;
        }
        isClose = true;

        if (type == 0) {
            animLike.start();
            objectY(imageLike);
        } else {
            animDis.start();

            objectX(imageDis);
        }
    }

    public void objectY(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        //animator.setRepeatCount(1);
        animator.setDuration(1000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setBackUp(); //执行回弹动画
            }
        });
    }

    public void objectX(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -10.0f, 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        // animator.setRepeatCount(1);
        animator.setDuration(1000);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setBackUp(); //执行回弹动画
            }
        });
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    //dp转px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
