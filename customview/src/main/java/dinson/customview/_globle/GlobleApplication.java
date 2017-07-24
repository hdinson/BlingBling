package dinson.customview._globle;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import dinson.customview.BuildConfig;


public class GlobleApplication extends Application {

    public static final Boolean IS_DEBUG = BuildConfig.DEBUG;

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();


        if (!IS_DEBUG) {
            CrashHandler.getInstance().init(context, context.getCacheDir() + "/CrashTxt/");
        }

    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
