package dinson.customview.widget;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatTextView;

import android.util.AttributeSet;

import com.dinson.blingbase.utils.TypefaceUtil;


import static dinson.customview._global.ConstantsUtils.APP_FONT_PATH;

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
        Typeface typeface = TypefaceUtil.getFontFromAssets(getContext(), APP_FONT_PATH);
        setTypeface(typeface);
    }
}
