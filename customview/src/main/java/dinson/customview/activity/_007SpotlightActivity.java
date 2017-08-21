package dinson.customview.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.LogUtils;
import dinson.customview.utils.TypefaceUtils;
import dinson.customview.weight.spotlight.CustomTarget;
import dinson.customview.weight.spotlight.SimpleTarget;
import dinson.customview.weight.spotlight.Spotlight;

import static dinson.customview.R.id.tv_type;

public class _007SpotlightActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__007_spotlight);


        initUI();
    }


    private void initUI() {
        Typeface typeface = TypefaceUtils.get(this, "fonts/FZLanTingHeiS_Regular.ttf");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_dis = (TextView) findViewById(R.id.tv_dis);
        mTvType = (TextView) findViewById(tv_type);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_title.setTypeface(typeface);
        tv_dis.setTypeface(typeface);
        mTvType.setTypeface(typeface);
        tv_name.setTypeface(typeface);
        tv_content.setTypeface(typeface);


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View ivImg = findViewById(R.id.iv_img);
        LogUtils.i(ivImg.getMeasuredHeight()+" "+ivImg.getMeasuredWidth());

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) return;
        View ivImg = findViewById(R.id.iv_img);
        float radius = (float) Math.hypot(ivImg.getWidth(), ivImg.getHeight())/2f;
        SimpleTarget firstTarget = new SimpleTarget.Builder(this).setPoint(ivImg)
            .setRadius(radius)
            .setTitle("御三家")
            .setDescription("小龟宝可梦")
            .build();

        int[] oneLocation = new int[2];
        mTvType.getLocationInWindow(oneLocation);
        float oneX = oneLocation[0] + mTvType.getWidth() / 4f * 3;
        float oneY = oneLocation[1] + mTvType.getHeight() / 2f;
        SimpleTarget secondTarget = new SimpleTarget.Builder(this).setPoint(oneX, oneY)
            .setRadius(100f)
            .setTitle("水系")
            .setDescription("特性：激流")
            .build();

        View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        CustomTarget thirdTarget = new CustomTarget.Builder(this).setPoint(cancel)
            .setRadius(100f)
            .setView(R.layout.layout_007_spotlight_custom)
            .build();

        Spotlight.with(this)
            .setDuration(1000L)
            .setAnimation(new DecelerateInterpolator(2f))
            .setTargets(firstTarget, secondTarget,thirdTarget)
            .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public int setWindowBackgroundColor() {
        return R.drawable._007_activity_bg;
    }
}
