package dinson.customview.widget._025provider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import dinson.customview.R
import dinson.customview.activity._025ChooseScheduleActivity
import dinson.customview.entity._025._025Schedule
import dinson.customview.kotlin.loge
import dinson.customview.utils.MMKVUtils


import kotlin.math.abs

open class WidgetProviderBig : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        loge { "onReceive  ${intent.action}" }
        when (intent.action) {
            REFRESH_ACTION -> {
                val scheduleId = intent.getStringExtra(EXTRA_SCHEDULE_ID)
                val widgetIds = intent.getIntArrayExtra(EXTRA_WIDGET_IDS)

                widgetIds.forEach {
                    if (mIdsSet.containsKey(it)) {
                        val value = mIdsSet[it]
                        if (scheduleId.isEmpty() || scheduleId == value) {
                            loge { "匹配了" }
                            val bean = MMKVUtils.getScheduleById(scheduleId)
                            toggleLayout(context, it, bean)
                        } else {
                            loge { "没匹配" }
                        }
                    } else {
                        loge { "找到一个未设置数据" }
                    }
                }
            }
            SET_DATA_ACTION -> {
                val widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, 0)
                val scheduleId = intent.getStringExtra(EXTRA_SCHEDULE_ID)
                mIdsSet[widgetId] = scheduleId
                loge { "mIdsSet : ${mIdsSet.toList()}" }
                val schedule = MMKVUtils.getScheduleById(scheduleId)
                toggleLayout(context, widgetId, schedule)
            }
        }
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        loge { "onUpdate : ${appWidgetIds.asList()}" }

        appWidgetIds.forEach {
            val views = RemoteViews(context.packageName, R.layout.widget_025_layout_big)
            setAddScheduleClick(context, it, views)
            appWidgetManager.updateAppWidget(it, views)
        }
    }

    open fun toggleLayout(context: Context, id: Int, schedule: _025Schedule? = null) {
        val views = RemoteViews(context.packageName, R.layout.widget_025_layout_big)
        loge { "schedule : $schedule" }
        if (schedule == null) {
            views.setTextViewText(R.id.tvScheduleName, "")
            views.setTextViewText(R.id.tvScheduleDateTime, "")
            views.setTextViewText(R.id.tvDayCount, "")
            views.setViewVisibility(R.id.emptyView, View.VISIBLE)
            setAddScheduleClick(context, id, views)
        } else {
            views.setTextViewText(R.id.tvScheduleName, schedule.name)
            views.setTextViewText(
                R.id.tvScheduleDateTime,
                schedule.dateTime + " " + schedule.getWeek()
            )
            views.setViewVisibility(R.id.emptyView, View.GONE)
            views.setTextViewText(R.id.tvDayCount, abs(schedule.getDisplayDay()).toString())
        }
        AppWidgetManager.getInstance(context).updateAppWidget(id, views)
    }

    open fun setAddScheduleClick(context: Context, id: Int, view: RemoteViews) {
        val intent = Intent(REFRESH_ACTION)
        intent.setClass(context, _025ChooseScheduleActivity::class.java)
        intent.putExtra(_025ChooseScheduleActivity.EXTRA_ID, id)
        intent.putExtra(_025ChooseScheduleActivity.EXTRA_FLAG, _025ChooseScheduleActivity.FLAG_BIG)
        val pendingIntent =
            PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        view.setOnClickPendingIntent(R.id.emptyView, pendingIntent)
    }


    companion object {

        const val REFRESH_ACTION = "action.REFRESH"
        const val SET_DATA_ACTION = "action.SET_DATA"
        const val EXTRA_SCHEDULE_ID = "extra_schedule_id"
        const val EXTRA_WIDGET_IDS = "extra_widget_ids"
        const val EXTRA_WIDGET_ID = "extra_widget_id"

        private val mIdsSet = HashMap<Int, String>()

        fun sendToRefresh(
            context: Context, widgetIds: IntArray = intArrayOf(),
            scheduleId: String = ""
        ) {
            val intent = Intent(REFRESH_ACTION)
            intent.setClass(context, WidgetProviderBig::class.java)
            if (widgetIds.isEmpty()) {
                val manager = AppWidgetManager.getInstance(context)
                val ids =
                    manager.getAppWidgetIds(ComponentName(context, WidgetProviderBig::class.java))
                intent.putExtra(EXTRA_WIDGET_IDS, ids)
            } else {
                intent.putExtra(EXTRA_WIDGET_IDS, widgetIds)
            }
            intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            context.sendBroadcast(intent)
        }

        fun sendToSetData(context: Context, widgetId: Int, scheduleId: String) {
            val intent = Intent(SET_DATA_ACTION)
            intent.setClass(context, WidgetProviderBig::class.java)
            intent.putExtra(EXTRA_WIDGET_ID, widgetId)
            intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            loge { "WidgetProviderBig sendToSetData widgetIds: $widgetId scheduleId: $scheduleId" }
            context.sendBroadcast(intent)
        }
    }

    //-----------------------------------------------------------------------------
    /**
     * onAppWidgetOptionsChanged
     * 当 Widget 第一次被添加或者大小发生变化时调用该方法，可以在此控制 Widget 元素的显示和隐藏。
     *
     *
     * onEnabled
     * 当 Widget 第一次被添加时调用，例如用户添加了两个你的 Widget，那么只有在添加第一个 Widget 时该方法会被调用。
     * 所以该方法比较适合执行你所有 Widgets 只需进行一次的操作
     *
     *
     * onDisabled
     * 与 onEnabled 恰好相反，当你的最后一个 Widget 被删除时调用该方法，所以这里用来清理之前在 onEnabled() 中进行的操作。
     *
     *
     * onDeleted
     * 当 Widget 被删除时调用该方法。
     */
}