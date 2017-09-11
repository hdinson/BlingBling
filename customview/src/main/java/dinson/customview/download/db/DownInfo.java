package dinson.customview.download.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import dinson.customview.download.api.DownloadServiceApi;
import dinson.customview.download.listener.DownloadListener;
import dinson.customview.download.DownState;

/**
 * apk下载请求数据基础类
 */

@Entity
public class DownInfo {
    @Id
    private long id;
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
    private DownloadListener listener;
    /*超时设置*/
    private int connectOnTime = 10;
    /*state状态数据库保存*/
    private int stateInt;
    /*url*/
    private String url;

    public DownInfo(String url, DownloadListener listener) {
        setUrl(url);
        setListener(listener);
    }

    public DownInfo(String url) {
        setUrl(url);
    }

    @Generated(hash = 2050590965)
    public DownInfo(long id, String savePath, long countLength, long readLength,
            int connectOnTime, int stateInt, String url) {
        this.id = id;
        this.savePath = savePath;
        this.countLength = countLength;
        this.readLength = readLength;
        this.connectOnTime = connectOnTime;
        this.stateInt = stateInt;
        this.url = url;
    }

    @Generated(hash = 928324469)
    public DownInfo() {
    }

    public DownState getState() {
        switch (getStateInt()) {
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInt(state.getState());
    }


    public int getStateInt() {
        return stateInt;
    }

    public void setStateInt(int stateInt) {
        this.stateInt = stateInt;
    }

    public DownloadListener getListener() {
        return listener;
    }

    public void setListener(DownloadListener listener) {
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

    public int getConnectOnTime() {
        return this.connectOnTime;
    }

    public void setConnectOnTime(int connectOnTime) {
        this.connectOnTime = connectOnTime;
    }
}
