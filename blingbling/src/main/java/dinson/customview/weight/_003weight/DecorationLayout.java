package dinson.customview.weight._003weight;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.io.File;

import dinson.customview.R;
import dinson.customview.utils.FileUtils;
import dinson.customview.utils.ToastUtils;
import dinson.customview.weight.imagewatcher.ImageWatcher;
import dinson.customview.weight.imagewatcher.ImageWatcherHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static dinson.customview.kotlin.LogExtentionKt.loge;
import static dinson.customview.kotlin.LogExtentionKt.logi;
import static dinson.customview.kotlin.LogExtentionKt.logv;


public class DecorationLayout extends FrameLayout implements ViewPager.OnPageChangeListener,
    ImageWatcher.OnShowListener, View.OnClickListener {

    private ImageWatcherHelper mHolder;
    private int currentPosition;
    private int mPagerPositionOffsetPixels;
    private View vDownload;

    public DecorationLayout(Context context) {
        this(context, null);
    }

    public DecorationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final FrameLayout vContainer = (FrameLayout) LayoutInflater.from(context)
            .inflate(R.layout.layout_003_download_pic, this);
        vDownload = vContainer.findViewById(R.id.ivDownload);
        vDownload.setOnClickListener(this);
    }

    public void attachImageWatcher(ImageWatcherHelper iwHelper) {
        mHolder = iwHelper;
    }

    @Override
    public void onClick(View v) {
        if (mPagerPositionOffsetPixels != 0) return;
        if (v.getId() == R.id.ivDownload) {
            v.setVisibility(View.GONE);
            String url = mHolder.getImageWatcher().getUri(currentPosition).toString();
            logi(() -> "-----: " + url);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            Disposable a = Observable
                .create((ObservableOnSubscribe<File>) e -> {
                    //通过gilde下载得到file文件,这里需要注意android.permission.INTERNET权限
                    e.onNext(Glide.with(getContext())
                        .downloadOnly()
                        .load(url).submit()
                        .get());
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(file -> {
                    //获取到下载得到的图片，进行本地保存
                    File pictureFolder = Environment.getExternalStorageDirectory();
                    //第二个参数为你想要保存的目录名称
                    File appDir = new File(pictureFolder, "BLingDownload");
                    if (!appDir.exists()) {
                        boolean b = appDir.mkdirs();
                        logv(() -> "文件夹创建:" + b);
                    }
                    File destFile = new File(appDir, fileName);
                    //把gilde下载得到图片复制到定义好的目录中去
                    logi(() -> "-----: " + file.getPath() + "  /  " + destFile.getPath());
                    boolean isSuccess = FileUtils.copyFile(file, destFile);
                    logi(() -> "isSuccess: " + isSuccess);
                    Looper.prepare();
                    ToastUtils.INSTANCE.showToast("图片已保存至BLingDownload/" + fileName);
                    logi(() -> destFile.exists() + "   :<-");
                    Looper.loop();

                    // 最后通知图库更新
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(destFile.getPath()))));
                }, throwable -> loge(throwable::toString));
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        mPagerPositionOffsetPixels = i1;
        notifyAlphaChanged(v);
    }

    @Override
    public void onPageSelected(int i) {
        resetDownloadBtn(i);
    }

    public void resetDownloadBtn(int index) {
        currentPosition = index;
        String url = mHolder.getImageWatcher().getUri(currentPosition).toString();
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        String path = Environment.getExternalStorageDirectory() + File.separator + "BLingDownload" + File.separator + fileName;
        boolean exists = new File(path).exists();
        logi(() -> path + " already exists: " + exists);
        vDownload.setVisibility(exists ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 滑动透明
     */
    private void notifyAlphaChanged(float p) {
        if (0 < p && p <= 0.2f) {
            float r = (0.2f - p) * 5;
            setAlpha(r);
        } else if (0.8f <= p && p < 1) {
            float r = (p - 0.8f) * 5;
            setAlpha(r);
        } else if (p == 0) {
            setAlpha(1f);
        } else {
            setAlpha(0f);
        }
    }


    @Override
    public void onShowed() {
        resetDownloadBtn(0);
    }
}
