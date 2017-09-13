package dinson.customview.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import dinson.customview.utils.LogUtils;

/**
 * UC首页自定义Behavior
 */

public class MainTvDrawerBehavior extends CoordinatorLayout.Behavior<View> {
    private int mFrameMaxHeight = 100;
    private int mStartY;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    public MainTvDrawerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (mStartY == 0) {
            mStartY = (int) dependency.getY() - child.getHeight();
        }
        float percent = (dependency.getY() - child.getHeight()) / mStartY;
        //改变child的坐标(从消失，到可见)
        child.setY(-child.getHeight() * percent);
        LogUtils.e("percent: " + percent + "  setY: " + (-child.getHeight() * percent));
        return true;
    }
}