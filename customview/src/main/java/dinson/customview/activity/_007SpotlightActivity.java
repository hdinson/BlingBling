package dinson.customview.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.TypefaceUtils;

public class _007SpotlightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__007_spotlight);


        initUI();

    }

    private void initUI() {

        Typeface typeface = TypefaceUtils.get(this, "fonts/FZLanTingHeiS_Regular.ttf");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_dis = (TextView) findViewById(R.id.tv_dis);
        TextView tv_type = (TextView) findViewById(R.id.tv_type);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_title.setTypeface(typeface);
        tv_dis.setTypeface(typeface);
        tv_type.setTypeface(typeface);
        tv_name.setTypeface(typeface);
        tv_content.setTypeface(typeface);

    }
}
