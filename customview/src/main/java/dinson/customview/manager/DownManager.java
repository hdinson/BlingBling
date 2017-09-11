package dinson.customview.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import dinson.customview.api.QiNiuApi;
import dinson.customview.http.HttpHelper;
import dinson.customview.model._009PanoramaImageModel;
import dinson.customview.utils.IOUtils;
import dinson.customview.utils.LogUtils;
import retrofit2.Response;

import static dinson.customview.manager.DownManager.DownloadState.DOWNLOADING;
import static dinson.customview.manager.DownManager.DownloadState.PAUSE;
import static dinson.customview.manager.DownManager.DownloadState.WAITING;

/**
 * 下载管理器 （观察者模式）
 *
 * @author Dinson - 2017/9/8
 */
public class DownManager {

    public enum DownloadState {
        UNDO, WAITING, DOWNLOADING, PAUSE, ERROR, SUCCESS
    }

    private static DownManager sDownManager;
    private ArrayList<DownloadObserver> mObservers = new ArrayList<>();//观察者集合
    private HashMap<String, _009PanoramaImageModel> mPanoramaImageMap = new HashMap<>();//下载对象的集合
    private HashMap<String, DownLoadTask> mDownLoadTask = new HashMap<>();//下载任务的集合


    private DownManager() {
    }

    public static DownManager getInstance() {
        if (sDownManager == null) {
            synchronized (DownManager.class) {
                if (sDownManager == null) {
                    sDownManager = new DownManager();
                }
            }
        }
        return sDownManager;
    }

    public void notifyDownloadState(DownloadState state) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadStateChanged(state);

        }
    }

    public void notifyDownloadProgress(float currentProgress) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadProgressChanged(currentProgress);
        }
    }

    public void registerObserver(DownloadObserver observer) {
        if (observer != null && !mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void unregisterObserver(DownloadObserver observer) {
        if (observer != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    public void download(_009PanoramaImageModel bean) {
        _009PanoramaImageModel imageModel = mPanoramaImageMap.get(bean.id);
        if (imageModel == null) {
            imageModel = bean;
        }
        imageModel.currentState = WAITING;
        notifyDownloadState(WAITING);
        mPanoramaImageMap.put(bean.id, imageModel);

        DownLoadTask task = new DownLoadTask(imageModel);
        ThreadManager.getThreadPool().execute(task);
        mDownLoadTask.put(imageModel.id, task);
    }

    //
    public void pause(_009PanoramaImageModel bean) {
        _009PanoramaImageModel imageBean = mPanoramaImageMap.get(bean.id);
        if (imageBean != null) {
            if (imageBean.currentState == WAITING || imageBean.currentState == DOWNLOADING) {
                imageBean.currentState = PAUSE;
                notifyDownloadState(PAUSE);

                DownLoadTask task = mDownLoadTask.get(imageBean.id);
                if (task != null) {
                    ThreadManager.getThreadPool().cancel(task);
                }
            }
        }
    }

    class DownLoadTask implements Runnable {
        private _009PanoramaImageModel mDownLoadBean;

        public DownLoadTask(_009PanoramaImageModel model) {
            this.mDownLoadBean = model;
        }

        @Override
        public void run() {
            LogUtils.i(String.format(Locale.CHINA, "%S is download... url: %s  localPath: %s",
                mDownLoadBean.title, mDownLoadBean.originalImg, mDownLoadBean.getFilePath()));

            mDownLoadBean.currentState = DOWNLOADING;
            File file = new File(mDownLoadBean.getFilePath());


            QiNiuApi qiNiuApi = HttpHelper.create(QiNiuApi.class);

            Response httpResult = null;

            try {
                if (!file.exists() || file.length() != mDownLoadBean.currentPos
                    || mDownLoadBean.currentPos == 0) {
                    //从头下载
                    file.delete();
                    mDownLoadBean.currentPos = 0;
                    httpResult = qiNiuApi.downloadPicFromNet(mDownLoadBean.originalImg).execute();
                } else {
                    //断点续传
                    httpResult = qiNiuApi.downloadPicFromNet(mDownLoadBean.originalImg + "&range=" + file.length()).execute();
                }
                if (httpResult != null && httpResult.raw() != null && httpResult.raw().body() != null) {
                    InputStream in = httpResult.raw().body().byteStream();
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file, true);
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        while ((len = in.read(buffer)) != -1 && mDownLoadBean.currentState == DOWNLOADING) {
                            out.write(buffer, 0, len);
                            out.flush();

                            //更新下载进度
                            mDownLoadBean.currentPos += len;
                            notifyDownloadProgress(mDownLoadBean.getProgress());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.close(in);
                        IOUtils.close(out);
                    }
                } else {
                    //网络异常
                    // TODO: 2017/9/8


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface DownloadObserver {
        void onDownloadStateChanged(DownloadState state);

        void onDownloadProgressChanged(float currentProgress);
    }
}
