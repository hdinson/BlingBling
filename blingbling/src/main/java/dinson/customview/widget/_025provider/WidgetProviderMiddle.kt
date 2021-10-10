package dinson.customview.widget._025provider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import dinson.customview.R
import dinson.customview.activity._025ChooseScheduleActivity
import dinson.customview.entity._025._025Schedule
import dinson.customview.kotlin.loge
import kotlin.math.abs


class WidgetProviderMiddle : WidgetProviderBig() {
    companion object {

        fun sendToRefresh(context: Context, widgetIds: IntArray = intArrayOf(),
                          scheduleId: String = "") {
            val intent = Intent(REFRESH_ACTION)
            intent.setClass(context, WidgetProviderMiddle::class.java)
            if (widgetIds.isEmpty()) {
                val manager = AppWidgetManager.getInstance(context)
                val ids = manager.getAppWidgetIds(ComponentName(context, WidgetProviderMiddle::class.java))
                intent.putExtra(EXTRA_WIDGET_IDS, ids)
            } else {
                intent.putExtra(EXTRA_WIDGET_IDS, widgetIds)
            }
            intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            context.sendBroadcast(intent)
        }

        fun sendToSetData(context: Context, widgetId: Int, scheduleId: String) {
            val intent = Intent(SET_DATA_ACTION)
            intent.setClass(context, WidgetProviderMiddle::class.java)
            intent.putExtra(EXTRA_WIDGET_ID, widgetId)
            intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            loge { "WidgetProviderMiddle sendToSetData widgetIds: $widgetId scheduleId: $scheduleId" }
            context.sendBroadcast(intent)
        }
    }

    override fun setAddScheduleClick(context: Context, id: Int, view: RemoteViews) {
        val intent = Intent(REFRESH_ACTION)
        intent.setClass(context, _025ChooseScheduleActivity::class.java)
        intent.putExtra(_025ChooseScheduleActivity.EXTRA_ID, id)
        intent.putExtra(_025ChooseScheduleActivity.EXTRA_FLAG, _025ChooseScheduleActivity.FLAG_MIDDLE)
        val pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        view.setOnClickPendingIntent(R.id.emptyView, pendingIntent)
    }

    override fun toggleLayout(context: Context, id: Int, schedule: _025Schedule?) {
        val views = RemoteViews(context.packageName, R.layout.widget_025_layout_middle)
        loge { "schedule : $schedule" }
        if (schedule == null) {
            views.setTextViewText(R.id.tvScheduleName, "")
            views.setTextViewText(R.id.tvScheduleDateTime, "")
            views.setTextViewText(R.id.tvDayCount, "")
            views.setViewVisibility(R.id.emptyView, View.VISIBLE)
            setAddScheduleClick(context, id, views)
        } else {
            views.setTextViewText(R.id.tvScheduleName, schedule.name)
            views.setTextViewText(R.id.tvScheduleDateTime, schedule.dateTime + " " + schedule.getWeek())
            views.setViewVisibility(R.id.emptyView, View.GONE)
            views.setTextViewText(R.id.tvDayCount, abs(schedule.getDisplayDay()).toString())
        }
        AppWidgetManager.getInstance(context).updateAppWidget(id, views)
    }

}