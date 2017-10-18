package dinson.customview.weight;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import dinson.customview.utils.TypefaceUtils;

/**
 * 兰亭字体的TextView
 */
public class LanTingFontTextView extends AppCompatTextView {
    public LanTingFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LanTingFontTextView(Context context) {
        super(context);
        init();
    }

    public LanTingFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        Typeface typeface = TypefaceUtils.get(getContext(), "fonts/FZLanTingHeiS_Regular.ttf");
        setTypeface(typeface);
    }
}
