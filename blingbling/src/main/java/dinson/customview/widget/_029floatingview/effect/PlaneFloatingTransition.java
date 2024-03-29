package dinson.customview.widget._029floatingview.effect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;

import dinson.customview.widget._029floatingview.spring.SimpleReboundListener;
import dinson.customview.widget._029floatingview.spring.SpringHelper;
import dinson.customview.widget._029floatingview.transition.BaseFloatingPathTransition;
import dinson.customview.widget._029floatingview.transition.FloatingPath;
import dinson.customview.widget._029floatingview.transition.PathPosition;
import dinson.customview.widget._029floatingview.transition.YumFloating;

/**
 * 飞机动画
 */
public class PlaneFloatingTransition extends BaseFloatingPathTransition {


    @Override
    public FloatingPath getFloatingPath() {
        Path path = new Path();
        path.moveTo(0, 0);
        path.quadTo(100, -300, 0, -600);
        path.rLineTo(0, -1920 - 300);
        return FloatingPath.create(path, false);
    }

    @Override
    public void applyFloating(final YumFloating yumFloating) {

        ValueAnimator translateAnimator = ObjectAnimator.ofFloat(getStartPathPosition(), getEndPathPosition());
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                PathPosition floatingPosition = getFloatingPosition(value);
                yumFloating.setTranslationX(floatingPosition.x);
                yumFloating.setTranslationY(floatingPosition.y);

            }
        });
        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                yumFloating.setTranslationX(0);
                yumFloating.setTranslationY(0);
                yumFloating.setAlpha(0f);
                yumFloating.clear();
            }
        });


        SpringHelper.createWithBouncinessAndSpeed(0.0f, 1.0f, 14, 15)
            .reboundListener(new SimpleReboundListener() {
                @Override
                public void onReboundUpdate(double currentValue) {
                    yumFloating.setScaleX((float) currentValue);
                    yumFloating.setScaleY((float) currentValue);
                }
            }).start(yumFloating);

        translateAnimator.setDuration(3000);
        translateAnimator.start();
    }
}