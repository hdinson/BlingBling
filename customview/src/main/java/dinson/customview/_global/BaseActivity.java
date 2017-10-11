package dinson.customview._global;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import dinson.customview.R;

/**
 * 所有activity的基类
 */
public class BaseActivity extends AppCompatActivity {
    private long mStartTime;
    private long mEndTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mStartTime= System.currentTimeMillis();
        super.onCreate(savedInstanceState);

        /*共享元素*/
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        /*透明状态栏*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getSupportActionBar().hide();
        /*activity的出现动画*/
        overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        /*logcat点击跳转对用activity*/
        logShowActivity();
    }
    @Override
    protected void onStart() {
        super.onStart();
        logShowActivity();
        getWindow().setBackgroundDrawableResource(setWindowBackgroundColor());
    }

    /**
     * 设置窗口背景颜色
     */
    public int setWindowBackgroundColor() {
        return R.color.window_bg;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (finishWithAnim())
            overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right);
    }

    public boolean finishWithAnim() {
        return true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        logShowActivity();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        logShowActivity();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        logShowActivity();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        logShowActivity();
    }

    @Override
    public void onLocalVoiceInteractionStarted() {
        super.onLocalVoiceInteractionStarted();
        logShowActivity();
    }

    @Override
    public void onLocalVoiceInteractionStopped() {
        super.onLocalVoiceInteractionStopped();
        logShowActivity();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logShowActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logShowActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logShowActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logShowActivity();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logShowActivity();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        logShowActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logShowActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logShowActivity();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logShowActivity();
    }

    private static void logShowActivity() {
        StackTraceElement[] stackTraceElement = Thread.currentThread()
            .getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo("logShowActivity") == 0) {
                currentIndex = i + 1;
                break;
            }
        }
        currentIndex += 1;
        String fullClassName = stackTraceElement[currentIndex].getClassName();
        if (!fullClassName.startsWith(ConstantsUtils.PACKAGE_NAME)) return;
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = stackTraceElement[currentIndex].getMethodName();
        String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());
        Log.v("dd", ConstantsUtils.LOGCAT_TAG + "at " + fullClassName + "." + methodName + "("
            + className + ".java:" + lineNumber + ")");
    }
}
