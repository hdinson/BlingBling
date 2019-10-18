package dinson.customview.weight;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import dinson.customview.utils.TypefaceUtils;

/**
 * IconFont支持
 */
public class IconFontTextView extends AppCompatTextView {
    public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IconFontTextView(Context context) {
        super(context);
        init();
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        Typeface typeface = TypefaceUtils.INSTANCE.getIconFont(getContext());
        setTypeface(typeface);
    }
}
