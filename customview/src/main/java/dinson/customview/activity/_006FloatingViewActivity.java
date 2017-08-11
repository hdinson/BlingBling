package dinson.customview.activity;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.TypefaceUtils;
import dinson.customview.utils.UIUtils;
import dinson.customview.weight.floatingview.Floating;
import dinson.customview.weight.floatingview.FloatingBuilder;
import dinson.customview.weight.floatingview.FloatingElement;
import dinson.customview.weight.floatingview.effect.EarthFloatingTransition;
import dinson.customview.weight.floatingview.effect.PlaneFloatingTransition;
import dinson.customview.weight.floatingview.effect.ScaleFloatingTransition;
import dinson.customview.weight.floatingview.effect.TranslateFloatingTransition;

public class _006FloatingViewActivity extends BaseActivity implements View.OnClickListener {

    private Floating mFloating;
    private Typeface mTypeface;
    private ConstraintLayout mRootLayout;
    private int mScreenHeight;
    private int mScreenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__006_floating_view);

        mTypeface = TypefaceUtils.get(this, "fonts/FZLanTingHeiS_Regular.ttf");
        mFloating = new Floating(this);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.iv_like).setOnClickListener(this);
        findViewById(R.id.iv_star).setOnClickListener(this);
        findViewById(R.id.iv_school).setOnClickListener(this);
        findViewById(R.id.iv_earth).setOnClickListener(this);
        findViewById(R.id.iv_plane).setOnClickListener(this);

        mRootLayout = (ConstraintLayout) findViewById(R.id.root);
        mScreenHeight = UIUtils.getScreenHeight(this);
        mScreenWidth = UIUtils.getScreenWidth(this);

    }


    private int mCount = 0;
    private boolean toggle = true;

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_like://点赞
                View layout = LayoutInflater.from(this).inflate(R.layout.layout_006_like, null);
                TextView tvNum = (TextView) layout.findViewById(R.id.tv_num);
                tvNum.setText(String.format(Locale.CHINA, "+%d", ++mCount));
                FloatingElement floatingElement = new FloatingBuilder()
                    .anchorView(v)
                    .targetView(layout)
                    .floatingTransition(new TranslateFloatingTransition())
                    .build();
                mFloating.startFloating(floatingElement);
                break;
            case R.id.iv_star://星星
                mRootLayout.setBackgroundColor(toggle ? Color.parseColor("#62a465") : Color.parseColor("#6d5f88"));
                Animator animator = ViewAnimationUtils.createCircularReveal(mRootLayout, v.getTop(), v.getLeft(),
                    0, (float) Math.hypot(mScreenWidth, mScreenHeight));
                animator.setDuration(700);
                animator.start();
                toggle = !toggle;
                break;
            case R.id.iv_school://黑板
                TextView textView = new TextView(_006FloatingViewActivity.this);
                textView.setText("4");
                int left = UIUtils.dip2px(20);
                int top = UIUtils.dip2px(10);
                textView.setTextSize(20);
                textView.setBackgroundResource(R.drawable.shadow_ret_r2);
                textView.setPadding(left, top, left, top);
                textView.setTypeface(mTypeface);

                FloatingElement floatingSchool = new FloatingBuilder()
                    .anchorView(v)
                    .targetView(textView)
                    .offsetY(-v.getMeasuredHeight())
                    .floatingTransition(new ScaleFloatingTransition(2000))
                    .build();
                mFloating.startFloating(floatingSchool);
                break;
            case R.id.iv_earth:
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(v.getMeasuredWidth(), v.getMeasuredHeight()));
                imageView.setImageResource(R.drawable._006_moon);

                FloatingElement floatingEarth = new FloatingBuilder()
                    .anchorView(v)
                    .targetView(imageView)
                    .floatingTransition(new EarthFloatingTransition())
                    .build();
                mFloating.startFloating(floatingEarth);
                break;
            case R.id.iv_plane:
                ImageView plane = new ImageView(this);
                plane.setLayoutParams(new ViewGroup.LayoutParams(v.getMeasuredWidth(), v.getMeasuredHeight()));
                plane.setImageResource(R.drawable._006_plane);

                FloatingElement floatingPlane = new FloatingBuilder()
                    .anchorView(v)
                    .targetView(plane)
                    .floatingTransition(new PlaneFloatingTransition())
                    .build();
                mFloating.startFloating(floatingPlane);
                break;

        }
    }
}
