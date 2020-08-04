package dinson.customview.model;

import com.dinson.blingbase.utils.StringUtils;

import org.jetbrains.annotations.NotNull;

import dinson.customview._global.ConstantsUtils;
import dinson.customview.download.model.DownloadInfo;
import dinson.customview.download.model.ITransformDownloadInfo;
;

/**
 * @author sunfusheng on 2017/8/25.
 */
public class _009PanoramaImageModel implements ITransformDownloadInfo {

    public String title;
    public String desc;
    private String originalImg;
    public String smallImg;
    public String localPath;

    private float size;
    private float currentPos;


    _009PanoramaImageModel(String title, String desc, String originalImg, String smallImg) {
        this.title = title;
        this.desc = desc;
        this.originalImg = originalImg;
        this.smallImg = smallImg;
        this.localPath = ConstantsUtils.INSTANCE.getSDCARD_PRIVATE_IMAGE() + StringUtils.getUrlName(originalImg);
    }

    public float getProgress() {
        if (size == 0) return 0;
        return currentPos / size;
    }

    @NotNull
    @Override
    public DownloadInfo transform() {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setSavePath(localPath);
        downloadInfo.setUrl(originalImg);
        return downloadInfo;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(float currentPos) {
        this.currentPos = currentPos;
    }
}
