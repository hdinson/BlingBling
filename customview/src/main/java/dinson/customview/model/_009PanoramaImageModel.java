package dinson.customview.model;

import dinson.customview.download.model.DownloadInfo;
import dinson.customview.download.model.ITransformDownloadInfo;
import dinson.customview.utils.StringUtils;

/**
 * @author sunfusheng on 2017/8/25.
 */
public class _009PanoramaImageModel implements ITransformDownloadInfo {

    public String title;
    public String desc;
    public String originalImg;
    public String smallImg;
    public String localPath;

    private int size;
    private int currentPos;


    public _009PanoramaImageModel(String title, String desc, String originalImg, String smallImg) {
        this.title = title;
        this.desc = desc;
        this.originalImg = originalImg;
        this.smallImg = smallImg;
        this.localPath = StringUtils.getUrlName(originalImg);
    }

    public float getProgress() {
        if (size == 0) return 0;
        return currentPos / (float) size;
    }

    @Override
    public DownloadInfo transform() {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setSavePath(localPath);
        downloadInfo.setUrl(originalImg);
        return downloadInfo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }
}
