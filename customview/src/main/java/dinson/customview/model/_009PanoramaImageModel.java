package dinson.customview.model;

import java.io.File;

import dinson.customview._global.ConstantsUtils;
import dinson.customview.manager.DownManager.DownloadState;
import dinson.customview.utils.MD5;

/**
 * @author sunfusheng on 2017/8/25.
 */
public class _009PanoramaImageModel {

    public String title;
    public String desc;
    public String originalImg;
    public String smallImg;

    public String id;//title的md5值
    public long size;
    public long currentPos;
    public DownloadState currentState;

    public _009PanoramaImageModel(String title, String desc, String originalImg, String smallImg) {
        this.title = title;
        this.desc = desc;
        this.originalImg = originalImg;
        this.smallImg = smallImg;

        //初始化
        this.id = MD5.encode(title);
    }

    public float getProgress() {
        if (size == 0) return 0;
        return currentPos / (float) size;
    }

    public String getFilePath() {
        return ConstantsUtils.SDCARD_ROOT + id;
    }

    public boolean localExist() {
        return new File(getFilePath()).exists();
    }
}
