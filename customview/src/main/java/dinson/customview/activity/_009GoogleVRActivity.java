package dinson.customview.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.File;
import java.util.List;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.adapter._009ContentAdapter;
import dinson.customview.download.DownloadManager;
import dinson.customview.download.listener.HttpDownOnNextListener;
import dinson.customview.download.model.DownloadInfo;
import dinson.customview.download.model.DownloadState;
import dinson.customview.download.utils.DbDownUtil;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.model._009_ModelUtil;
import dinson.customview.utils.LogUtils;
import dinson.customview.utils.UIUtils;
import dinson.customview.weight.recycleview.LinearItemDecoration;
import dinson.customview.weight.recycleview.OnItemClickListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

public class _009GoogleVRActivity extends BaseActivity implements OnItemClickListener.OnClickListener {

    private List<_009PanoramaImageModel> mListDatas;
    private _009ContentAdapter mAdapter;
    private VrPanoramaView vrPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__009_google_vr);

        initView();
    }

    private void initView() {

        //顶部图片
        ImageView iv_img = (ImageView) findViewById(R.id.iv_img);
        String imgUrl = "http://ondlsj2sn.bkt.clouddn.com/Fqp3F7wLs2rjSrMlA-9bSIIi27Of.webp";
        RequestOptions rOptions = new RequestOptions()
            .error(R.drawable.def_img).diskCacheStrategy(DiskCacheStrategy.DATA);
        DrawableTransitionOptions tOptions = new DrawableTransitionOptions().crossFade(500);
        Glide.with(this).load(imgUrl).transition(tOptions).apply(rOptions).into(iv_img);

        vrPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);
        vrPanoramaView.setTouchTrackingEnabled(true);
        vrPanoramaView.setFullscreenButtonEnabled(true);
        vrPanoramaView.setInfoButtonEnabled(false);
        vrPanoramaView.setStereoModeButtonEnabled(false);

        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        mListDatas = _009_ModelUtil.getPanoramaImageList();
        mAdapter = new _009ContentAdapter(this, mListDatas);
        rvContent.addItemDecoration(new LinearItemDecoration(this));
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setItemAnimator(null);
        rvContent.addOnItemTouchListener(new OnItemClickListener(this, rvContent, this));
        rvContent.setAdapter(mAdapter);

    }

    private void loadPanoramaImage(_009PanoramaImageModel model) {
        File file = new File(model.localPath);
        LogUtils.e(String.format("File exists? %s and the path is %s", file.exists(), file.getAbsoluteFile()));

        Observable.just(model.localPath)
            .map(s -> Glide.with(UIUtils.getContext()).asBitmap().load(s)
                .submit(SIZE_ORIGINAL, SIZE_ORIGINAL).get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::loadPanoramaImage);
    }

    private void loadPanoramaImage(Bitmap bitmap) {
        if (bitmap == null) return;
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        options.inputType = VrPanoramaView.Options.TYPE_MONO;
        int childCount = vrPanoramaView.getChildCount();
        if (childCount > 1) for (int i = 1; i < childCount; i++) vrPanoramaView.removeViewAt(i);
        vrPanoramaView.loadImageFromBitmap(bitmap, options);

    }

    @Override
    public void onItemClick(View view, int position) {
        _009PanoramaImageModel selector = mListDatas.get(position);
        DownloadInfo transform = selector.transform();
        DownloadInfo downloadInfo = DbDownUtil.getInstance().queryDownBy(transform.getUrl());
        if (downloadInfo == null) {
            transform.setState(DownloadState.START);
            transform.setSavePath(selector.localPath);
            transform.setListener(new HttpDownOnNextListener() {
                @Override
                public void onComplete() {
                    mAdapter.notifyItemChanged(position);
                }

                @Override
                public void updateProgress(long readLength, long countLength) {
                    selector.setCurrentPos(readLength);
                    selector.setSize(countLength);
                    mAdapter.notifyItemChanged(position);
                }
            });
            DownloadManager.getInstance().startDown(transform);
        } else {
            loadPanoramaImage(selector);
        }
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


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawable(null);
    }
}
