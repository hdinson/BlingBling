package dinson.customview.download1.helper;

/**
 * Created by hly on 16/6/15.
 * email hugh_hly@sina.cn
 */
public interface GetFileCountListener {
    void success(boolean isSupportMulti, boolean isNew, String modified, Long fileSize);

    void failed();
}
