package dinson.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import dinson.customview.R;

public class _009GoogleVRActivity extends AppCompatActivity {

    private RecyclerView mRvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__009_google_vr);


        initView();
//        initListener();
    }

    private void initView() {
        /*ivMine = (ImageView) findViewById(R.id.iv_mine);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ImageUtil.colorImageViewDrawable(ivMine, R.color.transparent60_white);

        vrPanoramaView = (VrPanoramaView) findViewById(vrPanoramaView);
        vrPanoramaView.setTouchTrackingEnabled(true);
        vrPanoramaView.setFullscreenButtonEnabled(true);
        vrPanoramaView.setInfoButtonEnabled(false);
        vrPanoramaView.setStereoModeButtonEnabled(false);
        currPosition = new Random().nextInt(ModelUtil.getPanoramaImageList().size());
        _009PanoramaImageModel model = ModelUtil.getPanoramaImageList().get(currPosition);
        loadPanoramaImage(model);

        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        mAdapter = new PanoramaImageAdapter(this, _009_ModelUtil.getPanoramaImageList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);*/
    }
}
