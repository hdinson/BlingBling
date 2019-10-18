package dinson.customview.weight;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatTextView;
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
        Typeface typeface = TypefaceUtils.INSTANCE.getAppleFont(getContext());
        setTypeface(typeface);
    }
}
