package dinson.customview.behavior;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
