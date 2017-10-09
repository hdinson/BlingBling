package dinson.customview.weight._010parallaxsplash;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import dinson.customview.R;

/**
 * @author Dinson - 2017/10/9
 */
public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    private List<ParallaxFragment> mFragments;
    private ParallaxAdapter mParallaxAdapter;
    private float containerWidth;


    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setUp(int... ids) {
        if (!(getContext() instanceof FragmentActivity)) {
            throw new RuntimeException("You must extends FragmentActivity");
        }
        mFragments = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ParallaxFragment f = new ParallaxFragment();
            Bundle args = new Bundle();
            //页面索引
            args.putInt("index", i);
            //Fragment中需要加载的布局文件id
            args.putInt("layoutId", ids[i]);
            f.setArguments(args);
            mFragments.add(f);
        }

        FragmentActivity activity = (FragmentActivity) getContext();
        ViewPager viewPager = new ViewPager(getContext());
        mParallaxAdapter = new ParallaxAdapter(activity.getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mParallaxAdapter);
        viewPager.setId(R.id.parallax_pager);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(viewPager, 0);
        viewPager.addOnPageChangeListener(this);
    }


    /***********************************  监听  ******************************************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //在翻页的过程中，不断根据视图的标签中对应的动画参数，改变视图的位置或者透明度
        //获取到进入的页面
        ParallaxFragment inFragment = null;
        try {
            inFragment = mFragments.get(position - 1);
        } catch (Exception e) {
        }

        //获取到退出的页面
        ParallaxFragment outFragment = null;
        try {
            outFragment = mFragments.get(position);
        } catch (Exception e) {
        }

        if (inFragment != null) {
            //获取Fragment上所有的视图，实现动画效果
            List<View> inViews = inFragment.getParallaxViews();

            if (inViews != null) {
                for (View view : inViews) {
                    //获取标签，从标签上获取所有的动画参数
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //translationY改变view的偏移位置，translationY=100，代表view在其原始位置向下移动100
                    //仔细观察进入的fragment中view从远处过来，不断向下移动，最终停在原始位置
                    view.setTranslationY((containerWidth - positionOffsetPixels) * tag.yIn);
                    view.setTranslationX((containerWidth - positionOffsetPixels) * tag.xIn);
                    view.setAlpha(1 - (positionOffsetPixels / containerWidth) * tag.alphaIn * 2);
                }
            }
        }

        if (outFragment != null) {
            List<View> outViews = outFragment.getParallaxViews();
            if (outViews != null) {
                for (View view : outViews) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //仔细观察退出的fragment中view从原始位置开始向上移动，translationY应为负数
                    view.setTranslationY(0 - positionOffsetPixels * tag.yOut);
                    view.setTranslationX(0 - positionOffsetPixels * tag.xOut);
                    view.setAlpha(1 - (positionOffsetPixels / containerWidth) * tag.alphaOut * 2);
                }
            }
        }

    }

    @Override
    public void onPageSelected(int position) {

    }


    @Override
    public void onPageScrollStateChanged(int state) {
        this.containerWidth = getWidth();
    }
}

