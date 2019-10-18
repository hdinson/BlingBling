package dinson.customview.weight._010parallaxsplash;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
