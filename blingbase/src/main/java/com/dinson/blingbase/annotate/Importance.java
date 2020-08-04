package com.dinson.blingbase.annotate;


import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Oreo不用Priority了，用importance
 * IMPORTANCE_NONE 关闭通知
 * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
 * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
 * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
 * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
 */
@RequiresApi(api = Build.VERSION_CODES.N)
@IntDef({
    NotificationManager.IMPORTANCE_UNSPECIFIED,
    NotificationManager.IMPORTANCE_MIN,
    NotificationManager.IMPORTANCE_LOW,
    NotificationManager.IMPORTANCE_DEFAULT,
    NotificationManager.IMPORTANCE_HIGH
})
@Retention(RetentionPolicy.SOURCE)
public @interface Importance {
}




