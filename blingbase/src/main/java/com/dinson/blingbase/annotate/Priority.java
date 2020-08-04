package com.dinson.blingbase.annotate;

import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RequiresApi(api = Build.VERSION_CODES.N)
@IntDef({
    NotificationCompat.PRIORITY_MIN,
    NotificationCompat.PRIORITY_LOW,
    NotificationCompat.PRIORITY_DEFAULT,
    NotificationCompat.PRIORITY_HIGH,
    NotificationCompat.PRIORITY_MAX,
})
@Retention(RetentionPolicy.SOURCE)
public @interface Priority {
}
