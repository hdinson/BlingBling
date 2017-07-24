package dinson.customview.weight.AspectRatioView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 自带宽高比属性的view
 */

public class AspectRatioViewPager extends ViewPager {

    private AspectRatioSettings mSettings;

    public AspectRatioViewPager(Context context) {
        this(context, null);
    }

    public AspectRatioViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSettings = new AspectRatioSettings(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = (int) (width / mSettings.getRatio());
        int heightMeasure = MeasureSpec.makeMeasureSpec(height, heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasure);
    }
}
