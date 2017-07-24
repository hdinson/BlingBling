package dinson.customview.utils;

import android.util.Log;

import dinson.customview._globle.GlobleApplication;

public class LogUtils {

    /**
     * 日志输出时的TAG
     */
    private static String mTag = "LogUtils";
    /**
     * 方便过滤
     */
    private static String FLAG = "--------->";

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void v(String msg) {
        if (GlobleApplication.IS_DEBUG) {
            Log.v(mTag, FLAG + msg);
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String msg) {
        if (GlobleApplication.IS_DEBUG) {
            Log.d(mTag, FLAG + msg);
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String msg) {
        if (GlobleApplication.IS_DEBUG) {
            Log.i(mTag, FLAG + msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String msg) {
        if (GlobleApplication.IS_DEBUG) {
            Log.w(mTag, FLAG + msg);
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */
    public static void w(Throwable tr) {
        if (GlobleApplication.IS_DEBUG) {
            Log.w(mTag, "", tr);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    public static void w(String msg, Throwable tr) {
        if (GlobleApplication.IS_DEBUG && null != msg) {
            Log.w(mTag, FLAG + msg, tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    public static void e(String msg) {
        if (GlobleApplication.IS_DEBUG) {
            Log.e(mTag, FLAG + msg);
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */
    public static void e(Throwable tr) {
        if (GlobleApplication.IS_DEBUG) {
            Log.e(mTag, "", tr);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    public static void e(String msg, Throwable tr) {
        if (GlobleApplication.IS_DEBUG && null != msg) {
            Log.e(mTag, FLAG + msg, tr);
        }
    }
}
