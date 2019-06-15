package dinson.customview.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ABLNoTouchBehavior extends AppBarLayout.Behavior {
    public ABLNoTouchBehavior() {
    }

    public ABLNoTouchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return true;
    }
}
