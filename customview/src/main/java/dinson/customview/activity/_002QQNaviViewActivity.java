package dinson.customview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.weight._002qqnaviview.QQNaviView;
import dinson.customview.weight._002qqnaviview.QQNaviViewManager;

public class _002QQNaviViewActivity extends BaseActivity {

    private QQNaviViewManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__002_qqnavi_view);

        initUI();
    }

    private void initUI() {
        String img_url = "http://ondlsj2sn.bkt.clouddn.com/Fm71mrw6NECZAitx83iMnoBXUvgW.jpg";
        Glide.with(this).load(img_url).into((ImageView) findViewById(R.id.iv_bg));

        QQNaviView bubble = findViewById(R.id.qq_view_bubble);
        mManager = new QQNaviViewManager();
        mManager.setCheckedView(bubble);
    }

    public void onClick(View view) {
        if (!(view instanceof QQNaviView)) return;

        if (mManager.isCanCheck(view)) {
            mManager.clearCurrentCheckedView();
            mManager.setCheckedView((QQNaviView) view);
        }
    }
}
