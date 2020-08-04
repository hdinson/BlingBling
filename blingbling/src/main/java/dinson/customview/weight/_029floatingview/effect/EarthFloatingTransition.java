package dinson.customview.weight._029floatingview.effect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;

import dinson.customview.weight._029floatingview.transition.BaseFloatingPathTransition;
import dinson.customview.weight._029floatingview.transition.FloatingPath;
import dinson.customview.weight._029floatingview.transition.PathPosition;
import dinson.customview.weight._029floatingview.transition.YumFloating;

/**
 * 地球动画
 */
public class EarthFloatingTransition extends BaseFloatingPathTransition {


    @Override
    public FloatingPath getFloatingPath() {
        Path path = new Path();
//        path.moveTo(-300, 0);
//        path.quadTo(0, -300, 300, 0);
//        path.quadTo(0, 600, -100, 0);
        path.addCircle(0, 0, 300, Path.Direction.CW);
        return FloatingPath.create(path, false);
    }

    @Override
    public void applyFloating(final YumFloating yumFloating) {
        ValueAnimator translateAnimator = ObjectAnimator.ofFloat(0, 2000);
        translateAnimator.setDuration(2000);
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                PathPosition floatingPosition = getFloatingPosition(value);
                yumFloating.setTranslationX(floatingPosition.x);
                yumFloating.setTranslationY(floatingPosition.y);

            }
        });


        ValueAnimator alphaAnimation = ObjectAnimator.ofFloat(1f,0f);
        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                yumFloating.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        alphaAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                yumFloating.clear();
            }
        });
        alphaAnimation.setStartDelay(1000);
        alphaAnimation.setDuration(1000);
        translateAnimator.start();
        alphaAnimation.start();
    }
}