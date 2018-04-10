package dinson.customview.weight._006floatingview;

import android.support.annotation.LayoutRes;
import android.view.View;

import dinson.customview.weight._006floatingview.effect.ScaleFloatingTransition;
import dinson.customview.weight._006floatingview.transition.FloatingTransition;


/**
 * Helped class for building {@link FloatingElement}
 */
public class FloatingBuilder {

    private FloatingElement mFloatingElement;

    public FloatingBuilder() {
        mFloatingElement = new FloatingElement();
        mFloatingElement.targetViewLayoutResId = -1;
    }

    public FloatingBuilder offsetX(int offsetX) {
        mFloatingElement.offsetX = offsetX;
        return this;
    }

    public FloatingBuilder offsetY(int offsetY) {
        mFloatingElement.offsetY = offsetY;
        return this;
    }


    public FloatingBuilder floatingTransition(FloatingTransition floatingTransition) {
        mFloatingElement.floatingTransition = floatingTransition;
        return this;
    }


    public FloatingBuilder anchorView(View view) {
        mFloatingElement.anchorView = view;
        return this;
    }

    public FloatingBuilder targetView(View view) {
        mFloatingElement.targetView = view;
        return this;
    }

    public FloatingBuilder targetView(@LayoutRes int layResId) {
        mFloatingElement.targetViewLayoutResId = layResId;
        return this;
    }

    public FloatingElement build() {

        if (mFloatingElement.targetView == null && mFloatingElement.targetViewLayoutResId == -1) {
            throw new NullPointerException("TargetView should not be null");
        }

        if (mFloatingElement.anchorView == null) {
            throw new NullPointerException("AnchorView should not be null");
        }

        if (mFloatingElement.floatingTransition == null) {
            mFloatingElement.floatingTransition = new ScaleFloatingTransition();
        }
        return mFloatingElement;
    }

}
