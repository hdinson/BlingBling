package dinson.customview.weight._010parallaxsplash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有视差动画的fragment
 */
public class ParallaxFragment extends Fragment {
    //此Fragment上所有的需要实现视差动画的视图
    private List<View> parallaxViews = new ArrayList<>();

    private ParallaxOrientation mCurrentOrientation;

    public static ParallaxFragment newInstance(int ids) {
        Bundle args = new Bundle();
        args.putInt("layoutId", ids);
        ParallaxFragment fragment = new ParallaxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater original, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        int layoutId = args.getInt("layoutId");
        //1.布局加载器将布局加载进来了
        //2.解析创建布局上所有的视图
        //3.自己搞定创建视图的过程
        //4.获取视图相关的自定义属性的值
        // ParallaxLayoutInflater inflater = new ParallaxLayoutInflater(original, getActivity(), this);
        LayoutInflater inflater = original.cloneInContext(original.getContext());
        LayoutInflaterCompat.setFactory(inflater, new ParallaxFactory(inflater, this));


        //LogUtils.e("ParallaxFragment init");


        return inflater.inflate(layoutId, null);
    }

    public List<View> getParallaxViews() {
        return parallaxViews;
    }

    public ParallaxOrientation getCurrentOrientation() {
        return mCurrentOrientation;
    }

    public void setCurrentOrientation(ParallaxOrientation orientation) {
        this.mCurrentOrientation = orientation;
    }
}
