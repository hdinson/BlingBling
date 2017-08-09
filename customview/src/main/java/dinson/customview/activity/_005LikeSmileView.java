package dinson.customview.activity;

import android.os.Bundle;
import android.view.View;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;

public class _005LikeSmileView extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        setContentView(R.layout.activity__005_like_smile_view);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.tv_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right);
                break;
        }
    }
}
