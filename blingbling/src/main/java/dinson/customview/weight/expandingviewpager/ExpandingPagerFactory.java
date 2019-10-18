package dinson.customview.weight.expandingviewpager;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.ViewGroup;

/**
 * Created by Qs on 16/6/20.
 */
public class ExpandingPagerFactory {

    public static ExpandingFragment getCurrentFragment(ViewPager viewPager) {
        if (viewPager.getAdapter() instanceof ExpandingViewPagerAdapter) {
            ExpandingViewPagerAdapter adapter = (ExpandingViewPagerAdapter) viewPager.getAdapter();
            Fragment fragment = adapter.getCurrentFragment();
            if (fragment instanceof ExpandingFragment) {
                return (ExpandingFragment) fragment;
            }
        }
        return null;
    }

    public static void setupViewPager(final ViewPager viewPager) {
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.width = ((Activity) viewPager.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 7 * 5;
        layoutParams.height = (int) ((layoutParams.width / 0.75));

        viewPager.setOffscreenPageLimit(2);

        if (viewPager.getParent() instanceof ViewGroup) {
            ViewGroup viewParent = ((ViewGroup) viewPager.getParent());
            viewParent.setClipChildren(false);
            viewPager.setClipChildren(false);
        }

        viewPager.setPageTransformer(true, new ExpandingViewPagerTransformer());
    }

    public static boolean onBackPressed(ViewPager viewPager) {
        ExpandingFragment expandingFragment = getCurrentFragment(viewPager);
        if (expandingFragment != null && expandingFragment.isOpened()) {
            expandingFragment.close();
            return true;
        }
        return false;
    }
}
