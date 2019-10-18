package dinson.customview.behavior;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * UC首页自定义Behavior
 */

public class DrawerBehavior extends CoordinatorLayout.Behavior<TextView> {
    private int mFrameMaxHeight = 100;
    private int mStartY;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    public DrawerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        //记录开始的Y坐标  也就是toolbar起始Y坐标
        if (mStartY == 0) {
            mStartY = (int) dependency.getY() - child.getHeight();
        }

        //计算toolbar从开始移动到最后的百分比
        float percent = (dependency.getY() - child.getHeight()) / mStartY;


        //改变child的坐标(从消失，到可见)
        child.setY(-child.getHeight() * percent);
//        LogUtils.e("childheight: " + child.getHeight() + " getY: " + dependency.getY());
        return true;
    }
}