package dinson.customview.weight._010parallaxsplash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dinson.customview.R;
import dinson.customview.utils.LogUtils;

/**
 * @author Dinson - 2017/10/9
 */
public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    private List<ParallaxFragment> mFragments;
    private float containerWidth;
    private ParallaxFragment mInFragment, mOutFragment;
    private ViewPager mViewPager;
    private ParallaxAdapter mAdapter;


    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void notifyDataSetChanged(ParallaxFragment... fragments) {
        if (mFragments == null) return;
        Collections.addAll(mFragments, fragments);
        mAdapter.notifyDataSetChanged();
    }

    public void setUp(int... ids) {
        if (!(getContext() instanceof FragmentActivity)) {
            throw new RuntimeException("You must extends FragmentActivity");
        }
        mFragments = new ArrayList<>();
        for (int id : ids) {
            ParallaxFragment f = ParallaxFragment.newInstance(id);
            mFragments.add(f);
        }

        FragmentActivity activity = (FragmentActivity) getContext();
        mViewPager = new ViewPager(getContext());
        mAdapter = new ParallaxAdapter(activity.getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setId(R.id.parallaxPager);
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(mViewPager, 0);
        mViewPager.addOnPageChangeListener(this);
    }


    /***********************************  监听  ******************************************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        LogUtils.i("onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");

        checkScrollOrientation(position);


        if (mInFragment != null) {
            //获取Fragment上所有的视图，实现动画效果
            List<View> inViews = mInFragment.getParallaxViews();
            boolean isLeftIn = (mInFragment.getCurrentOrientation() == ParallaxOrientation.LEFT);

            if (inViews != null) {

                boolean isCurrentPagerChanged = positionOffset == 0 && positionOffsetPixels == 0;

                for (View view : inViews) {
                    //获取标签，从标签上获取所有的动画参数
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallaxViewTag);
                    if (tag == null) {
                        continue;
                    }

                    if (!isLeftIn && !isCurrentPagerChanged) {
                        //translationY改变view的偏移位置，translationY=100，代表view在其原始位置向下移动100
                        //仔细观察进入的fragment中view从远处过来，不断向下移动，最终停在原始位置
                        view.setTranslationY((positionOffsetPixels - containerWidth) * tag.yIn);
                        view.setTranslationX((positionOffsetPixels - containerWidth) * tag.xIn);
                        if (tag.alphaIn != 0 || tag.alphaOut != 0)
                            view.setAlpha(positionOffsetPixels * tag.alphaIn * 2 / containerWidth);
                    } else if (isLeftIn && !isCurrentPagerChanged) {
                        view.setTranslationY(positionOffsetPixels * tag.yOut);
                        view.setTranslationX(positionOffsetPixels * tag.xOut);
                        if (tag.alphaIn != 0 || tag.alphaOut != 0)
                            view.setAlpha(1 - positionOffsetPixels * tag.alphaIn * 2 / containerWidth);
                    } else {
                        view.setAlpha(1);
                        view.setTranslationX(0);
                        view.setTranslationY(0);
                    }
                }
            }
        }

        if (mOutFragment != null) {
            List<View> outViews = mOutFragment.getParallaxViews();
            boolean isLeftOut = (mOutFragment.getCurrentOrientation() == ParallaxOrientation.LEFT);

            if (outViews != null) {
                for (View view : outViews) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallaxViewTag);
                    if (tag == null) {
                        continue;
                    }

                    if (isLeftOut) {
                        //仔细观察退出的fragment中view从原始位置开始向上移动，translationY应为负数
                        view.setTranslationY(positionOffsetPixels * tag.yOut);
                        view.setTranslationX(positionOffsetPixels * tag.xOut);
                        if (tag.alphaIn != 0 || tag.alphaOut != 0)
                            view.setAlpha(1 - positionOffsetPixels * tag.alphaIn * 2 / containerWidth);
                    } else {
                        view.setTranslationY((positionOffsetPixels - containerWidth) * tag.yIn);
                        view.setTranslationX((positionOffsetPixels - containerWidth) * tag.xIn);
                        if (tag.alphaIn != 0 || tag.alphaOut != 0)
                            view.setAlpha(positionOffsetPixels / tag.alphaIn / containerWidth);
                    }

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
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                currentPosition = mViewPager.getCurrentItem();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                mOrientation = 0;
                break;
        }
    }


    /**
     * 根据滑动方向的不同选择不同的fragment
     *
     * @param position 滑动监听回调的位置
     */
    private void checkScrollOrientation(int position) {
        //初始时判断方向
        if (mOrientation == 0) {
            mOrientation = position == currentPosition ? 1 : -1;
            boolean leftPagerVisible = isLeftPagerVisible();
            //在翻页的过程中，不断根据视图的标签中对应的动画参数，改变视图的位置或者透明度
            //获取到进入的页面
            mInFragment = getFragmentQuietly(leftPagerVisible ? position : position + 1);
            if (mInFragment != null)
                mInFragment.setCurrentOrientation(leftPagerVisible ? ParallaxOrientation.LEFT : ParallaxOrientation.RIGHT);
            //获取到退出的页面
            mOutFragment = getFragmentQuietly(leftPagerVisible ? position + 1 : position);
            if (mOutFragment != null)
                mOutFragment.setCurrentOrientation(leftPagerVisible ? ParallaxOrientation.RIGHT : ParallaxOrientation.LEFT);
            LogUtils.e("mInFragment:" + mInFragment + "   mOutFragment:" + mOutFragment);
            return;
        }

        int temp = position == currentPosition ? 1 : -1;
        if (mOrientation != temp) {
            //方向改变了
            mOrientation = temp;
            //在翻页的过程中，不断根据视图的标签中对应的动画参数，改变视图的位置或者透明度
            //获取到进入的页面
            mInFragment = getFragmentQuietly(mOrientation == 1 ? position + 1 : position);
            //获取到退出的页面
            mOutFragment = getFragmentQuietly(mOrientation == 1 ? position : position + 1);

            if (mInFragment != null)
                mInFragment.setCurrentOrientation(isLeftPagerVisible() ? ParallaxOrientation.LEFT : ParallaxOrientation.RIGHT);
            if (mOutFragment != null)
                mOutFragment.setCurrentOrientation(isLeftPagerVisible() ? ParallaxOrientation.RIGHT : ParallaxOrientation.LEFT);
            //LogUtils.e("mInFragment:" + mInFragment + "   mOutFragment:" + mOutFragment);
        }
    }

    /**
     * 安静的获取fragment
     */
    private ParallaxFragment getFragmentQuietly(int position) {
        try {
            return mFragments.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    private int mOrientation;//0表示没有方向，-1表示向左，1表示向右
    private int currentPosition;//viewpager当前的位置

    /**
     * 判断是否滑向左边
     */
    private boolean isLeftPagerVisible() {
        return mOrientation == -1;
    }
}