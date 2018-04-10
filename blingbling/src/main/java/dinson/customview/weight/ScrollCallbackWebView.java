package dinson.customview.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 当页面滑动时会回调的WebView
 * 由于{@link WebView.OnScrollChangeListener} API必须大于23才能用
 * 所以重写webView,自己暴露出来
 */
public class ScrollCallbackWebView extends WebView {
    public ScrollCallbackWebView(Context context) {
        super(context);
    }

    public ScrollCallbackWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollCallbackWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null) {
            callback.onScroll(l, t, oldl, oldt);
        }
    }

    private onScrollChangeCallback callback;

    public interface onScrollChangeCallback {
        void onScroll(int l, int t, int oldl, int oldt);
    }

    public void setOnscrollChangeCallback(onScrollChangeCallback callback) {
        this.callback = callback;
    }

}
