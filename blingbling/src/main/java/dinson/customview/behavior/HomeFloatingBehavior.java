package dinson.customview.behavior;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 首页监听recycleview的滑动
 */
public class HomeFloatingBehavior extends Behavior<View> {

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
        if (percent>1)percent=1;
        child.setScaleX(1-percent);
        child.setScaleY(1-percent);
        return true;
    }
}
