package dinson.customview.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

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
