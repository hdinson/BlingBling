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


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        CrashHandler.INSTANCE.init(ConstantsUtils.INSTANCE.getSDCARD_PRIVATE());

    }


    public static Context getContext() {
        return context;
    }

}
