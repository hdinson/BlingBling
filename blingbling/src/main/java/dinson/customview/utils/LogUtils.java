package dinson.customview.utils;

import android.util.Log;

import java.util.Locale;

import dinson.customview._global.ConstantsUtils;
import dinson.customview._global.GlobalApplication;

public class LogUtils {

    /**
     * 日志输出时的TAG
     */
    private static String mTag = "LogUtils";
    /**
     * 方便过滤
     */
    private static String FLAG = ConstantsUtils.LOGCAT_TAG;


    private static String getLineNumber(String methodName) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo(methodName) == 0) {
                currentIndex = i + 1;
                break;
            }
        }
        String fullClassName = stackTraceElement[currentIndex].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());
        return ".(" + className + ".java:" + lineNumber + ")";
    }

    /**
     * 以级别为 v 的形式输出LOG
     */
    public static void v(String msg) {
        if (GlobalApplication.IS_DEBUG) {
            Log.v(mTag, FLAG + getLineNumber("v") + msg);
        }
    }

    /**
     * 以级别为 v 的形式输出LOG
     */
    public static void v(String msg, boolean showLine) {
        if (GlobalApplication.IS_DEBUG) {
            Log.v(mTag, String.format("%s%s", showLine ? getLineNumber("v") : "", FLAG + msg));
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String msg) {
        if (GlobalApplication.IS_DEBUG) {
            Log.d(mTag, FLAG + getLineNumber("d") + msg);
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    public static void d(String msg, boolean showLine) {
        if (GlobalApplication.IS_DEBUG) {
            Log.d(mTag, String.format("%s%s", showLine ? getLineNumber("d") : "", FLAG + msg));
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String msg) {
        if (GlobalApplication.IS_DEBUG) {
            Log.i(mTag, FLAG + getLineNumber("i") + msg);
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String msg, boolean showLine) {
        if (GlobalApplication.IS_DEBUG) {
            Log.i(mTag, String.format(Locale.CHINA, "%s%s", FLAG + msg
                , showLine ? getLineNumber("i") : ""));
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String msg) {
        if (GlobalApplication.IS_DEBUG) {
            Log.w(mTag, FLAG + getLineNumber("w") + msg);
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    public static void w(String msg, boolean showLine) {
        if (GlobalApplication.IS_DEBUG) {
            Log.w(mTag, String.format(Locale.CHINA, "%s%s", FLAG + msg
                , showLine ? getLineNumber("w") : ""));
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */


    public static void e(String msg) {
        if (GlobalApplication.IS_DEBUG) {
            Log.e(mTag, FLAG + getLineNumber("e") + msg);
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    public static void e(String msg, boolean showLine) {
        if (GlobalApplication.IS_DEBUG) {
            Log.e(mTag, String.format(Locale.CHINA, "%s%s", FLAG + msg
                , showLine ? getLineNumber("e") : ""));
        }
    }
}
