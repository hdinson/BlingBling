package dinson.customview._global;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import dinson.customview.BuildConfig;


public class GlobalApplication extends Application {

    public static final Boolean IS_DEBUG = BuildConfig.DEBUG;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

        CrashHandler.INSTANCE.init(ConstantsUtils.INSTANCE.getSDCARD_PRIVATE());

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
