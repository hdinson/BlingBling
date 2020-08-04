package dinson.customview.weight;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatTextView;

import android.util.AttributeSet;

import com.dinson.blingbase.utils.TypefaceUtil;

import dinson.customview._global.ConstantsUtils;
;

import static dinson.customview._global.ConstantsUtils.ICON_FONT_PATH;

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
        Typeface typeface = TypefaceUtil.getFontFromAssets(getContext(), ICON_FONT_PATH);
        setTypeface(typeface);
    }
}
