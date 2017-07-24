package dinson.customview.weight;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


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


    public void init() {
        final Typeface iconfont = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "iconfont/iconfont.ttf");
        setTypeface(iconfont);
    }
}
