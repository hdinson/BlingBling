package dinson.customview.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 首页监听recycleview的滑动
 */
public class HomeFloatingBehavior extends Behavior<View> {
    private int mFrameMaxHeight = 100;
    private int mStartY;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    public HomeFloatingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (mStartY == 0) {
            mStartY = (int) dependency.getY();
        }
        float percent = (mStartY - dependency.getY()) / 500;
        //改变child的坐标(从消失，到可见)
        child.setAlpha(1-percent);
        return true;
    }
}
