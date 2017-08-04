package dinson.customview._global;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import dinson.customview.R;
import dinson.customview.utils.UIUtils;

/**
 * 所有activity的基类
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //共享元素
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getSupportActionBar().hide();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawable(UIUtils.getDrawable(R.color.window_bg));
    }
}
