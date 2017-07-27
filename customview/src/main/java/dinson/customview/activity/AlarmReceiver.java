package dinson.customview.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dinson.customview.utils.DateUtils;
import dinson.customview.utils.LogUtils;

/**
 * @author Dinson - 2017/7/27
 */
public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        LogUtils.i(intent.getAction());
        LogUtils.i(DateUtils.int2Str(DateUtils.getCurrentTimeMillis10()));
        LogUtils.i("收到广播");
        if ("android.alarm.demo.action".equals(intent.getAction())) {
            //第1步中设置的闹铃时间到，这里可以弹出闹铃提示并播放响铃
            //可以继续设置下一次闹铃时间;
            LogUtils.i("我是闹钟，我要叫醒你...");

        }
    }
}
