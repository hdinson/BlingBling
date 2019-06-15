package dinson.customview.weight.expandingviewpager;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Qs on 16/5/30.
 */
public class ExpandingViewPagerTransformer implements ViewPager.PageTransformer {

      private static final float MAX_SCALE = 0.9f;
      private static final float MIN_SCALE = 0.8f;

    @Override
    public void transformPage(@NonNull View page, float position) {

        position = position < -1 ? -1 : position;
        position = position > 1 ? 1 : position;

        float tempScale = position < 0 ? 1 + position : 1 - position;
       //((CardView) page).setCardElevation( 60);
        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}
