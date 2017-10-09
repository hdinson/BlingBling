package dinson.customview.weight._010parallaxsplash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Dinson - 2017/10/9
 */
public class ParallaxAdapter extends FragmentPagerAdapter {
    private List<ParallaxFragment> mFragments;

    public ParallaxAdapter(FragmentManager fm) {
        super(fm);
    }


    public ParallaxAdapter(FragmentManager supportFragmentManager, List<ParallaxFragment> fragments) {
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
