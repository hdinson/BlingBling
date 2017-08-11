package dinson.customview.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import dinson.customview.R;
import dinson.customview._global.GlobalApplication;


public class UIUtils {

    public static Context getContext() {
        return GlobalApplication.getContext();
    }

    public static Handler getHandler() {
        return GlobalApplication.getHandler();
    }

    public static int getMainThreadId() {
        return GlobalApplication.getMainThreadId();
    }

    // /////////////////加载资源文件 ///////////////////////////

    // 获取字符串
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    // 获取字符串数组
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    // 获取图片
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    // 获取颜色
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    // 获取尺寸
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);// 返回具体像素值
    }


    // /////////////////加载布局文件//////////////////////////
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    // /////////////////判断是否运行在主线程//////////////////////////
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }

        return false;
    }

    // 运行在主线程
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            r.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }

    private static Toast mToast;

    public static void showToast(String message) {
        if (null == mToast) {
            mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.getView().setBackgroundResource(R.drawable.toast_bg);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    private static Snackbar mSnackbar;

    public static void showSnackbar(View view, String message) {
        if (null == mSnackbar) {
            mSnackbar= Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        } else {
            mSnackbar.setText(message);
        }
        mSnackbar.show();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    public static int dip2px(  float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
}
