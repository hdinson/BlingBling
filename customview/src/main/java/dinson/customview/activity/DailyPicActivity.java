package dinson.customview.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.entity.one.DailyDetail;
import dinson.customview.utils.UIUtils;

public class DailyPicActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_pic);

        initUI();
    }

    private static final String EXTRA_DATA = "data";
    private static final String EXTRA_PATH = "path";

    public static void start(Context context, DailyDetail.DataBean bean, String imgPath, ActivityOptionsCompat options) {
        Intent starter = new Intent(context, DailyPicActivity.class);
        starter.putExtra(EXTRA_DATA, bean);
        starter.putExtra(EXTRA_PATH, imgPath);
        context.startActivity(starter, options.toBundle());
    }

    private void initUI() {

        DailyDetail.DataBean data = getIntent().getParcelableExtra(EXTRA_DATA);
        String path = getIntent().getStringExtra(EXTRA_PATH);
        ImageView iv_img = (ImageView) findViewById(R.id.iv_img);
        iv_img.setOnClickListener(this);

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        int screenWidth = UIUtils.getScreenWidth(DailyPicActivity.this);
        int height = screenWidth * bitmap.getHeight() / bitmap.getWidth();
        ViewGroup.LayoutParams para = iv_img.getLayoutParams();
        para.height = height;
        para.width = screenWidth;
        iv_img.setImageBitmap(bitmap);
        iv_img.setOnClickListener(this);

        String content = data.getHp_content();
        int indexOfStop = content.lastIndexOf("。");
        int indexOfQm = content.lastIndexOf("？");
        int index = indexOfStop > indexOfQm ? indexOfStop : indexOfQm;

        String subContent = content.substring(0, index + 1);
        String subName = content.substring(index + 1).trim();

        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        TextView tv_name = (TextView) findViewById(R.id.tv_title);

        tv_name.setText(subName);
        tv_content.setText(subContent);

        LinearLayout llContent = (LinearLayout) findViewById(R.id.ll_content);
        llContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (top < 0) return;

                v.removeOnLayoutChangeListener(this);
                //动画操作
                int height = bottom - top;
                int width = right - left;

                Animator animator = ViewAnimationUtils.createCircularReveal(v, 0, height, 0, (float) Math.hypot(height, width));
                animator.setDuration(700);
                animator.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
