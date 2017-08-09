package dinson.customview.weight.aspectratioview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 自带宽高比属性的view
 */

public class AspectRatioRecycleView extends RecyclerView {

    private AspectRatioSettings mSettings;

    public AspectRatioRecycleView(Context context) {
        this(context, null);
    }

    public AspectRatioRecycleView(Context context, AttributeSet attrs) {
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
