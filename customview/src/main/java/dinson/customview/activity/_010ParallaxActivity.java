package dinson.customview.activity;

import android.os.Bundle;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.fragment._010LoginFragment;
import dinson.customview.weight._010parallaxsplash.ParallaxContainer;
import dinson.customview.weight._010parallaxsplash.ParallaxFragment;

public class _010ParallaxActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__010__parallax);

        ParallaxContainer container = (ParallaxContainer) findViewById(R.id.parallax_container);
        container.setUp(R.layout.layout_010_splash1, R.layout.layout_010_splash2);

        ParallaxFragment f = _010LoginFragment.newInstance(R.layout.layout_010_splash3);
        container.notifyDataSetChanged(f);
    }

    @Override
    public int setWindowBackgroundColor() {
        return R.color._010_window_bg;
    }
}
