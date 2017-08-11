package dinson.customview.weight.floatingview.effect;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import dinson.customview.weight.floatingview.spring.SimpleReboundListener;
import dinson.customview.weight.floatingview.spring.SpringHelper;
import dinson.customview.weight.floatingview.transition.FloatingTransition;
import dinson.customview.weight.floatingview.transition.YumFloating;

public class ScaleFloatingTransition implements FloatingTransition {

    private long mDuration;
    private double mBounciness;
    private double mSpeed;

    public ScaleFloatingTransition() {
        mDuration = 1000;
        mBounciness = 10;
        mSpeed = 15;
    }

    public ScaleFloatingTransition(long duration) {
        this.mDuration = duration;
        mBounciness = 10;
        mSpeed = 15;
    }

    public ScaleFloatingTransition(long duration, double bounciness, double speed) {
        this.mDuration = duration;
        this.mBounciness = bounciness;
        this.mSpeed = speed;
    }

    @Override
    public void applyFloating(final YumFloating yumFloating) {
        
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(1.0f, 0.0f);
        alphaAnimator.setDuration(mDuration);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                yumFloating.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        alphaAnimator.start();

        SpringHelper.createWithBouncinessAndSpeed(0.0f, 1.0f, mBounciness, mSpeed)
                .reboundListener(new SimpleReboundListener(){
                    @Override
                    public void onReboundUpdate(double currentValue) {
                        yumFloating.setScaleX((float) currentValue);
                        yumFloating.setScaleY((float) currentValue);
                    }
                }).start(yumFloating);
    }
    
}
