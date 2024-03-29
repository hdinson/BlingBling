package dinson.customview.widget._010parallaxsplash;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import dinson.customview.R;

/**
 * 自定义视差LayoutInflater
 */
class ParallaxFactory implements LayoutInflaterFactory {
    private ParallaxFragment fragment;
    private LayoutInflater inflater;
    private final String[] sClassPrefix = {
        "android.widget.",
        "android.view."
    };

    ParallaxFactory(LayoutInflater inflater, ParallaxFragment fragment) {
        this.fragment = fragment;
        this.inflater = inflater;
    }

    private void setViewTag(View view, Context context, AttributeSet attrs) {
        //获取
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.View);
        if (a != null && a.length() > 0) {
            //获取自定义属性的值
            ParallaxViewTag tag = new ParallaxViewTag();
            tag.alphaIn = a.getFloat(R.styleable.View_a_in, 0f);
            tag.alphaOut = a.getFloat(R.styleable.View_a_out, 0f);
            tag.xIn = a.getFloat(R.styleable.View_x_in, 0f);
            tag.xOut = a.getFloat(R.styleable.View_x_out, 0f);
            tag.yIn = a.getFloat(R.styleable.View_y_in, 0f);
            tag.yOut = a.getFloat(R.styleable.View_y_out, 0f);

            //index
            if (!tag.isBlank()) {
                view.setTag(R.id.parallaxViewTag, tag);
                fragment.getParallaxViews().add(view);
            }
        }

        assert a != null;
        a.recycle();
    }

    private View createViewOrFailQuietly(String name, String prefix, AttributeSet attrs) {
        try {
            //通过系统的inflater创建视图，读取系统的属性
            return inflater.createView(name, prefix, attrs);
        } catch (Exception e) {
            return null;
        }
    }

    private View createViewOrFailQuietly(String name, AttributeSet attrs) {
        //1.自定义控件标签名称带点，所以创建时不需要前缀
        if (name.contains(".")) {
            createViewOrFailQuietly(name, null, attrs);
        }
        //2.系统视图需要加上前缀
        for (String prefix : sClassPrefix) {
            View view = createViewOrFailQuietly(name, prefix, attrs);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createViewOrFailQuietly(name, attrs);
        //实例化完成
        if (view != null) {
            //获取自定义属性，通过标签关联到视图上
            setViewTag(view, context, attrs);
        }
        return view;
    }
}