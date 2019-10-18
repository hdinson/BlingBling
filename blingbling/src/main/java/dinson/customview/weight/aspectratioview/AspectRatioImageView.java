package dinson.customview.weight.aspectratioview;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * 自带宽高比属性的view
 */
public class AspectRatioImageView extends AppCompatImageView {

    private AspectRatioSettings mSettings;

    public AspectRatioImageView(Context context) {
        this(context, null);
    }


    public AspectRatioImageView(Context context, AttributeSet attrs) {
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
