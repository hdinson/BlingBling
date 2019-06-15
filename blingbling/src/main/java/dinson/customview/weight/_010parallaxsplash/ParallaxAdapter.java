package dinson.customview.weight._010parallaxsplash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 视差fragment适配器
 */
class ParallaxAdapter extends FragmentPagerAdapter {
    private List<ParallaxFragment> mFragments;

    ParallaxAdapter(FragmentManager supportFragmentManager, List<ParallaxFragment> fragments) {
        super(supportFragmentManager);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
