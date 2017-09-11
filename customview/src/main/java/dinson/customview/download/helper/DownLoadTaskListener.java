package dinson.customview.download.helper;


import dinson.customview.download.db.DownLoadEntity;

/**
 * Created by hly on 16/4/11.
 * email hugh_hly@sina.cn
 */
public interface DownLoadTaskListener {
    void onStart();

    void onCancel(DownLoadEntity downLoadEntity);

    void onDownLoading(long downSize);

    void onCompleted(DownLoadEntity downLoadEntity);

    void onError(DownLoadEntity downLoadEntity, Throwable throwable);
}
