package dinson.customview.download.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import dinson.customview.api.DownloadServiceApi;
import dinson.customview.download.listener.HttpDownOnNextListener;

/**
 * apk下载请求数据基础类
 * Created by WZG on 2016/10/20.
 */

@Entity
public class DownloadInfo {
    @Id
    private long id;
    /*url*/
    private String url;
    /*存储位置*/
    private String savePath;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*下载唯一的HttpService*/
    @Transient
    private DownloadServiceApi service;
    /*回调监听*/
    @Transient
    private HttpDownOnNextListener listener;
    /*超时设置*/
    private int connectonTime = 6;
    /*state状态数据库保存*/
    private int stateInte;


    public DownloadInfo(String url, HttpDownOnNextListener listener) {
        setUrl(url);
        setListener(listener);

    }

    public DownloadInfo(String url, String savePath, HttpDownOnNextListener listener) {
        setUrl(url);
        setSavePath(savePath);
        setListener(listener);
    }

    public DownloadInfo(String url) {
        setUrl(url);
    }

    @Generated(hash = 1980428819)
    public DownloadInfo(long id, String url, String savePath, long countLength, long readLength, int connectonTime,
            int stateInte) {
        this.id = id;
        this.url = url;
        this.savePath = savePath;
        this.countLength = countLength;
        this.readLength = readLength;
        this.connectonTime = connectonTime;
        this.stateInte = stateInte;
    }

    @Generated(hash = 327086747)
    public DownloadInfo() {
    }

    public DownloadState getState() {
        switch (getStateInte()) {
            case 0:
                return DownloadState.START;
            case 1:
                return DownloadState.DOWN;
            case 2:
                return DownloadState.PAUSE;
            case 3:
                return DownloadState.STOP;
            case 4:
                return DownloadState.ERROR;
            case 5:
            default:
                return DownloadState.FINISH;
        }
    }

    public void setState(DownloadState state) {
        setStateInte(state.getState());
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public HttpDownOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpDownOnNextListener listener) {
        this.listener = listener;
    }

    public DownloadServiceApi getService() {
        return service;
    }

    public void setService(DownloadServiceApi service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }


    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getConnectonTime() {
        return this.connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }
}
