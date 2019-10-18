package dinson.customview.behavior;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import dinson.customview.utils.LogUtils;

/**
 * 首页监听recycleview的滑动
 */
public class _003Behavior extends Behavior<ImageView> {


    private float mOriginHeight;
    private float mAvatorHeight;


    public _003Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 需要监听哪些控件或者容器的状态
     *
     * @param parent     父容器
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    /**
     * 当监听的view发生改变的时候回调
     * 可以在此方法里面做一些相应的联动效果
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        init(parent, child, dependency);
        if (child.getY() <= 0) {
            if (percent != 0) {
                ViewCompat.setScaleX(child, 0);
                ViewCompat.setScaleY(child, 0);
                child.setElevation(0);
                percent = 0;
                LogUtils.e("头像隐藏");
            }
            return false;
        } else if (child.getY() >= mAvatorHeight) {
            return false;
        }
        float percent = child.getY() / mOriginHeight;
        if (this.percent == percent) return false;
        this.percent = percent;
        ViewCompat.setScaleX(child, percent);
        ViewCompat.setScaleY(child, percent);
        LogUtils.e("percent=" + percent + " OriginHeight=" + mOriginHeight + " currentHeight=" + child.getY());
        return false;

    }

    private void init(CoordinatorLayout parent, ImageView child, View dependency) {
        if (mOriginHeight == 0) mOriginHeight = dependency.getY();
        if (mAvatorHeight == 0) mAvatorHeight = child.getY();
    }

    float percent = 0;
}
