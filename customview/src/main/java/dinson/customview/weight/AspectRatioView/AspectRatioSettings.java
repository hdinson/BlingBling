package dinson.customview.weight.AspectRatioView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dinson.customview.R;


/**
 * 自带宽高比属性的view
 */
public class AspectRatioSettings {

    private float ratio;

    AspectRatioSettings(Context context, AttributeSet attrs) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AspectRatio, 0, 0);
        ratio = styledAttributes.getFloat(R.styleable.DiagonalLayout_diagonal_angle, 1);
        styledAttributes.recycle();
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
