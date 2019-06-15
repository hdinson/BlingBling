package dinson.customview.model;

import com.qiniu.storage.model.FileInfo;

/**
 * 七牛云服务器文件实体
 */
public class QiNiuFileInfo {
    private String key;
    private String hash;
    private long fsize;
    private long putTime;
    private String mimeType;
    private String endUser;

    //新增宽高属性用于瀑布流
    private int height;
    private int width;

    private String finalUrl;//文件的最终名字，因为私有空间需要加上下载凭证，否则无法访问

    public QiNiuFileInfo(FileInfo info, String finalUrl) {
        this.key = info.key;
        this.endUser = info.endUser;
        this.fsize = info.fsize;
        this.putTime = info.putTime;
        this.mimeType = info.mimeType;
        this.hash = info.hash;

        this.finalUrl = finalUrl;//域名
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getFsize() {
        return fsize;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }

    public long getPutTime() {
        return putTime;
    }

    public void setPutTime(long putTime) {
        this.putTime = putTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEndUser() {
        return endUser;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public int getHeight() {
        return height;
    }


    public int getWidth() {
        return width;
    }

    public boolean isNull() {
        return height == 0 || width == 0;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String getFinalUrl() {
        return finalUrl;
    }
}
