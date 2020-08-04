package com.dinson.blingbase.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FLAG_SHOW_LIGHTS
import androidx.core.app.NotificationManagerCompat
import com.dinson.blingbase.annotate.Importance
import com.dinson.blingbase.annotate.Priority
import com.dinson.blingbase.kotlin.getAppName

@Suppress("unused")
class RxNotification(val builder: Builder) {

    fun show() {
        val managerCompat = NotificationManagerCompat.from(builder.context)
        val notification = NotificationCompat.Builder(builder.context, builder.mChannelId.toString())
            .setContentText(builder.content)
            .setSmallIcon(builder.smallIcon, 2)
            .setPriority(builder.mPriority)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(builder.mAutoCancel)
            .setOngoing(builder.mOngoing)
            .setWhen(System.currentTimeMillis())//设置通知时间，默认为系统发出通知的时间，通常不用设置
        if (builder.mRemoteView != null) {
            //notification.setCustomBigContentView(builder.mRemoteView)
            notification.setContent(builder.mRemoteView)
        } else {
            notification.setContentText(builder.title)
        }
        if (builder.mProgress != null) {
            val mCurrent = (builder.mProgress!! * 100).toInt()
            if (mCurrent == 0) {
                notification.setProgress(0, 0, false)
            } else {
                notification.setProgress(100, mCurrent, false)
            }
            Log.i("TAG", "show: ${mCurrent},")
        }
        if (builder.mIndeterminate != null && builder.mIndeterminate!!) {
            notification.setProgress(0, 0, true)
        }
        if (!builder.mEnableVibration) notification.setVibrate(longArrayOf(0L))
        builder.mSubTitle?.apply { notification.setSubText(this) }//副标题
        builder.mTickerText?.apply { notification.setTicker(this) }
        builder.mSmallIconBgColor?.apply { notification.color = this }
        builder.mPendingIntent?.apply { notification.setFullScreenIntent(this, true) }
        builder.mSoundRes?.apply { notification.setSound(Uri.parse("android.resource://${builder.context.packageName}/$this")) }
        builder.mVibrate?.apply { notification.setVibrate(this) }
        builder.mLightsColor?.apply { notification.setLights(this, builder.mOnMs!!, builder.mOffMs!!) }
        if (builder.mLargeIcon != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification.setLargeIcon(BitmapFactory.decodeResource(builder.context.resources, builder.mLargeIcon!!))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Oreo不用Priority了，用importance
            //IMPORTANCE_NONE 关闭通知
            //IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
            //IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
            //IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
            //IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
            val channel = NotificationChannel(builder.mChannelId.toString(), builder.mChannelName, builder.mImportance)
            channel.setShowBadge(builder.mShowBadge)//长按桌面图标显示通知
            //震动
            if (!builder.mEnableVibration) channel.vibrationPattern = longArrayOf(0L)
            channel.enableVibration(builder.mEnableVibration)

            //通知闪光
            channel.enableLights(builder.mEnableLights)
            if (builder.mEnableLights) channel.lightColor = builder.mLightsColor!!

            //通知铃声
            builder.mSoundRes?.apply {
                // 设置提示音，IMPORTANCE_DEFAULT及以上才会有声音
                val uri = Uri.parse("android.resource://${builder.context.packageName}/$this")
                channel.setSound(uri, AudioAttributes.Builder().build())
            }
            managerCompat.createNotificationChannel(channel)
            notification.setChannelId(builder.mChannelId.toString())
        }
        val build = notification.build()
        if (builder.mEnableLights) {
            build.flags = build.flags or FLAG_SHOW_LIGHTS
        }
        managerCompat.notify(builder.mChannelId, build)
    }

    class Builder(val context: Context, val title: String, val content: String,
                  @DrawableRes val smallIcon: Int) {

        /**
         * 默认配置
         */
        var mChannelId = 0
        var mChannelName = context.getAppName()
        var mAutoCancel = true
        var mOngoing = false
        var mEnableLights = false
        var mEnableVibration = true
        var mShowBadge = true

        @RequiresApi(Build.VERSION_CODES.N)
        @Priority
        var mPriority = NotificationCompat.PRIORITY_DEFAULT

        @RequiresApi(Build.VERSION_CODES.O)
        @Importance
        var mImportance = NotificationManager.IMPORTANCE_DEFAULT

        /**
         * 默认空值
         */
        var mPendingIntent: PendingIntent? = null
        var mSubTitle: String? = null
        var mTickerText: String? = null
        var mVibrate: LongArray? = null
        var mLightsColor: Int? = null
        var mOnMs: Int? = null
        var mOffMs: Int? = null
        var mIndeterminate: Boolean? = null
        var mRemoteView: RemoteViews? = null

        @RawRes
        var mSoundRes: Int? = null

        @DrawableRes
        var mLargeIcon: Int? = null

        @ColorInt
        var mSmallIconBgColor: Int? = null

        @FloatRange(from = 0.0, to = 1.0)
        var mProgress: Float? = null


        /**
         * 设置自定义布局
         */
        fun setRemoteView(remoteView: RemoteViews): Builder {
            mRemoteView = remoteView
            return this
        }

        /**
         * 设置通知时，灯关颜色
         * @param lightsColor 颜色 args
         * @param onMs 亮多少毫秒
         * @param offMs 熄灭多少毫秒
         */
        fun setLightsColor(lightsColor: Int, onMs: Int, offMs: Int): Builder {
            mEnableLights = true
            mLightsColor = lightsColor
            mOnMs = onMs
            mOffMs = offMs
            return this
        }

        /**
         * 设置声音资源
         */
        fun setSoundRes(@RawRes sound: Int): Builder {
            mSoundRes = sound
            return this
        }

        /**
         * 设置不确定进度条
         */
        fun setIndeterminateProgress(): Builder {
            mIndeterminate = true
            return this
        }

        /**
         * 设置通知栏进度
         */
        fun setProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float): Builder {
            mProgress = progress
            return this
        }

        /**
         * 设置震动频率  eg: {0,500,500,500} 延迟0ms 振500ms 延迟500ms 再振500ms
         */
        fun setVibrate(vibrate: LongArray): Builder {
            mVibrate = vibrate
            return this
        }

        /**
         * 是否在长按桌面图标时，显示此渠道的通知
         */
        fun setShowBadge(isShow: Boolean): Builder {
            mShowBadge = isShow
            return this
        }

        /**
         * 设置是否展示桌面图标右上角通知小点（默认显示红色）
         */
        fun setEnableLights(enable: Boolean): Builder {
            mEnableLights = enable
            return this
        }

        /**
         * 设置是否震动（默认震动）
         */
        fun setEnableVibration(enable: Boolean): Builder {
            mEnableVibration = enable
            return this
        }


        /**
         * 设置通知渠道名
         */
        fun setChannelName(channelName: String): Builder {
            mChannelName = channelName
            return this
        }

        /**
         * 设置通知渠道ID
         */
        fun setChannelId(channelId: Int): Builder {
            mChannelId = channelId
            return this
        }

        /**
         * 设置大图标
         */
        fun setLargeIcon(@DrawableRes largeIcon: Int): Builder {
            this.mLargeIcon = largeIcon
            return this
        }

        /**
         * 设置点击通知跳转Intent
         */
        fun setPendingIntent(pendingIntent: PendingIntent): Builder {
            mPendingIntent = pendingIntent
            return this
        }

        /**
         * 设置通知第一次显示时，标题栏滚动的文本
         */
        fun setTickerText(tickerText: String): Builder {
            mTickerText = tickerText
            return this
        }

        /**
         * 设置通知优先级
         */
        fun setPriority(@Priority priority: Int): Builder {
            mPriority = priority
            return this
        }

        /**
         * 设置AndroidO 悬浮窗通知优先级
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun setImportance(@Importance importance: Int): Builder {
            mImportance = importance
            return this
        }

        /**
         * 设置小图标背景颜色
         */
        fun setSmallIconBgColor(@ColorInt argb: Int): Builder {
            mSmallIconBgColor = argb
            return this
        }

        /**
         * 设置是否点击消失
         */
        fun setIsAutoCancel(autoCancel: Boolean): Builder {
            mAutoCancel = autoCancel
            return this
        }

        /**
         * 设置是否常驻通知栏
         */
        fun setIsOngoing(ongoing: Boolean): Builder {
            mOngoing = ongoing
            return this
        }

        fun build(): RxNotification {
            return RxNotification(this)
        }
    }
}