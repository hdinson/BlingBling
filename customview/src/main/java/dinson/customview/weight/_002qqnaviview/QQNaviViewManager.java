package dinson.customview.weight._002qqnaviview;

import android.view.View;

/**
 * @author Dinson
 * @date 2017/6/30
 */
public class QQNaviViewManager {

    private QQNaviView currentCheckedView;

    /**
     * 设置当前点击的控件
     */
    public void setCheckedView(QQNaviView view) {
        this.currentCheckedView = view;
        view.setChecked(true);
    }

    /**
     * 清除当前点击的控件
     */
    public void clearCurrentCheckedView() {
        if (currentCheckedView != null)
            this.currentCheckedView.setChecked(false);
        this.currentCheckedView = null;
    }

    /**
     * 判断当前是否可以选中
     */
    public boolean isCanCheck(View view) {
        return currentCheckedView != view;
    }


    /**
     * 初始化
     */
    public void initCurrentLayout() {
        if (currentCheckedView != null) {
            currentCheckedView = null;
        }
    }
}
