package dinson.customview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.weight._005likesmileview.LikeSmileView;

public class _005LikeSmileViewActivity extends BaseActivity implements View.OnClickListener {

    private LikeSmileView mLikeSmile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__005_like_smile_view);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.tvBack).setOnClickListener(this);

        //顶部图片
        ImageView iv_img = (ImageView) findViewById(R.id.ivImg);
        String imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Ft8BnkO6CU83GxS2ZaSWXtMc_AQm.webp";
        RequestOptions rOptions = new RequestOptions()
            .error(R.drawable.def_img).diskCacheStrategy(DiskCacheStrategy.DATA);
        DrawableTransitionOptions tOptions = new DrawableTransitionOptions().crossFade(500);
        Glide.with(this).load(imgUrl).transition(tOptions).apply(rOptions).into(iv_img);

        mLikeSmile = (LikeSmileView) findViewById(R.id.likeSmile);
        mLikeSmile.setNum(66, 25);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLikeSmile.onDestroy();
    }
}
