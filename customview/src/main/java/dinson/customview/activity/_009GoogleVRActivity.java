package dinson.customview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import dinson.customview.R;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.model._009_ModelUtil;

public class _009GoogleVRActivity extends AppCompatActivity {

    private RecyclerView mRvContent;
    private VrPanoramaView vrPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__009_google_vr);


        initView();
//        initListener();
    }

    private void initView() {
        vrPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);
        vrPanoramaView.setTouchTrackingEnabled(true);
        vrPanoramaView.setFullscreenButtonEnabled(true);
        vrPanoramaView.setInfoButtonEnabled(false);
        vrPanoramaView.setStereoModeButtonEnabled(false);
        int currPosition = new Random().nextInt(_009_ModelUtil.getPanoramaImageList().size());
        _009PanoramaImageModel model = _009_ModelUtil.getPanoramaImageList().get(currPosition);
        loadPanoramaImage(model);

        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        /*mAdapter = new PanoramaImageAdapter(this, _009_ModelUtil.getPanoramaImageList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);*/
    }

    private void loadPanoramaImage(_009PanoramaImageModel model) {
        loadPanoramaImage(getBitmapFromAssets(model.assetName));
    }

    private void loadPanoramaImage(Bitmap bitmap) {
        if (bitmap == null) return;
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        options.inputType = VrPanoramaView.Options.TYPE_MONO;
        vrPanoramaView.loadImageFromBitmap(bitmap, options);
    }

    private Bitmap getBitmapFromAssets(String fileName) {
        if (TextUtils.isEmpty(fileName)) return null;
        try {
            InputStream inputStream = getAssets().open(fileName);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrPanoramaView.resumeRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vrPanoramaView.pauseRendering();
    }

    @Override
    protected void onDestroy() {
        vrPanoramaView.shutdown();
        super.onDestroy();
    }
}
