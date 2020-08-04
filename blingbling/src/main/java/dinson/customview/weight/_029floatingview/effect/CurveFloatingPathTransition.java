

package dinson.customview.weight._029floatingview.effect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;

import dinson.customview.weight._029floatingview.spring.SimpleReboundListener;
import dinson.customview.weight._029floatingview.spring.SpringHelper;
import dinson.customview.weight._029floatingview.transition.BaseFloatingPathTransition;
import dinson.customview.weight._029floatingview.transition.FloatingPath;
import dinson.customview.weight._029floatingview.transition.PathPosition;
import dinson.customview.weight._029floatingview.transition.YumFloating;


public class CurveFloatingPathTransition extends BaseFloatingPathTransition {

    private Path mPath;

    public CurveFloatingPathTransition() {
        
    }

    public CurveFloatingPathTransition(Path path) {
        this.mPath = path;
    }

    @Override
    public FloatingPath getFloatingPath() {
        if (mPath == null){
            mPath = new Path();
            mPath.moveTo(0, 0);
            mPath.quadTo(-100, -200, 0, -300);
            mPath.quadTo(200, -400, 0, -500);
        }
        return FloatingPath.create(mPath, false);
    }

    @Override
    public void applyFloating(final YumFloating yumFloating) {
        ValueAnimator translateAnimator;
        ValueAnimator alphaAnimator;

        
        translateAnimator = ObjectAnimator.ofFloat(getStartPathPosition(), getEndPathPosition());
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


        alphaAnimator = ObjectAnimator.ofFloat(1.0f, 0f);
        alphaAnimator.setDuration(3000);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                yumFloating.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        
        SpringHelper.createWithBouncinessAndSpeed(0.0f, 1.0f,11, 15)
                .reboundListener(new SimpleReboundListener(){
                    @Override
                    public void onReboundUpdate(double currentValue) {
                        yumFloating.setScaleX((float) currentValue);
                        yumFloating.setScaleY((float) currentValue);
                    }
                }).start(yumFloating);  
        
        translateAnimator.setDuration(3000);
        translateAnimator.setStartDelay(50);
        translateAnimator.start();
        alphaAnimator.start();
    }
    
}
