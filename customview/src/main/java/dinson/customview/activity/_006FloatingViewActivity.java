package dinson.customview.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.TypefaceUtils;
import dinson.customview.weight.floatingview.Floating;
import dinson.customview.weight.floatingview.FloatingBuilder;
import dinson.customview.weight.floatingview.FloatingElement;
import dinson.customview.weight.floatingview.effect.ScaleFloatingTransition;

public class _006FloatingViewActivity extends BaseActivity implements View.OnClickListener {

    private Floating mFloating;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__006_floating_view);

        mTypeface = TypefaceUtils.get(this, "fonts/FZLanTingHeiS_Regular.ttf");
        mFloating = new Floating(this);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.iv_school).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_school://黑板
                TextView textView = new TextView(_006FloatingViewActivity.this);
                textView.setText("4");
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(mTypeface);

                FloatingElement floatingElement = new FloatingBuilder()
                    .anchorView(v)
                    .targetView(textView)
                    .offsetY(-v.getMeasuredHeight())
                    .floatingTransition(new ScaleFloatingTransition(2000))
                    .build();
                mFloating.startFloating(floatingElement);
                break;
        }
    }
}
