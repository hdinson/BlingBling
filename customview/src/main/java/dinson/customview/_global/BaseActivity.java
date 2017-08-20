package dinson.customview._global;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import dinson.customview.R;
import dinson.customview.utils.UIUtils;

/**
 * 所有activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //共享元素
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         //getSupportActionBar().hide();
        //activity的出现动画
        overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawable(UIUtils.getDrawable(R.color.window_bg));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right);
    }
}
