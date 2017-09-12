package dinson.customview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.adapter._009ContentAdapter;
import dinson.customview.download.model.DownloadInfo;
import dinson.customview.download.utils.DbDownUtil;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.model._009_ModelUtil;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.UIUtils;
import dinson.customview.weight.recycleview.LinearItemDecoration;
import dinson.customview.weight.recycleview.OnItemClickListener;

public class _009GoogleVRActivity extends BaseActivity implements OnItemClickListener.OnClickListener {

    private RecyclerView mRvContent;
    private List<_009PanoramaImageModel> mListDatas;
    private _009ContentAdapter mAdapter;
    //private VrPanoramaView vrPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__009_google_vr);


        initView();
//        initListener();
    }

    private void initView() {

        //顶部图片
        ImageView iv_img = (ImageView) findViewById(R.id.iv_img);
        String imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Fqp3F7wLs2rjSrMlA-9bSIIi27Of.webp";
        GlideUtils.setImage(this, imgUrl, iv_img);


        /*vrPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);
        vrPanoramaView.setTouchTrackingEnabled(true);
        vrPanoramaView.setFullscreenButtonEnabled(true);
        vrPanoramaView.setInfoButtonEnabled(false);
        vrPanoramaView.setStereoModeButtonEnabled(false);
        int currPosition = new Random().nextInt(_009_ModelUtil.getPanoramaImageList().size());
        _009PanoramaImageModel model = _009_ModelUtil.getPanoramaImageList().get(currPosition);
        loadPanoramaImage(model);*/

        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        mListDatas = _009_ModelUtil.getPanoramaImageList();
        mAdapter = new _009ContentAdapter(this, mListDatas);
        mRvContent.addItemDecoration(new LinearItemDecoration(this));
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addOnItemTouchListener(new OnItemClickListener(this, mRvContent, this));
        mRvContent.setAdapter(mAdapter);

        //获取cpu个数
        int cpucount = Runtime.getRuntime().availableProcessors();
    }

    private void loadPanoramaImage(_009PanoramaImageModel model) {
        // loadPanoramaImage(getBitmapFromAssets(model.localPath));
    }

    private void loadPanoramaImage(Bitmap bitmap) {
        /*if (bitmap == null) return;
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        options.inputType = VrPanoramaView.Options.TYPE_MONO;
        vrPanoramaView.loadImageFromBitmap(bitmap, options);*/
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
    public void onItemClick(View view, int position) {
        _009PanoramaImageModel selector = mListDatas.get(position);
        DownloadInfo transform = selector.transform();
        DownloadInfo downloadInfo = DbDownUtil.getInstance().queryDownBy(transform.getUrl());
        if (downloadInfo == null) {
            UIUtils.showToast("没找到");
        } else {
            UIUtils.showToast("找到了：" + downloadInfo.getUrl());

            selector.title = "你真棒";
            mAdapter.notifyItemChanged(position);
        }
    }

    /*@Override
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
    }*/
}
