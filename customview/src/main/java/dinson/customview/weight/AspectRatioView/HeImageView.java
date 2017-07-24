package dinson.customview.weight.AspectRatioView;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * 自带宽高比属性的view
 */
public class HeImageView extends AppCompatImageView {

    private AspectRatioSettings mSettings;

    public HeImageView(Context context) {
        this(context, null);
    }

    public HeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
