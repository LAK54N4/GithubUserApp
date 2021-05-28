package com.laksana.githubuser.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import com.laksana.githubuser.R

class GitFavoriteWidget : AppWidgetProvider() {
    companion object {
        const val ACTION_TOAST: String = "actionToast"
        const val EXTRA_ITEM_POSITION = "extraItemPosition"
        const val ACTION_REFRESH = "actionRefresh"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {

            val serviceIntent = Intent(context, FavoriteWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            val clickIntent = Intent(context, GitFavoriteWidget::class.java)
            clickIntent.action = ACTION_TOAST
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val clickPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0)

            val views = RemoteViews(context.packageName, R.layout.git_favorite_widget)
            views.setRemoteAdapter(R.id.list_view, serviceIntent)
            views.setEmptyView(R.id.list_view, R.id.empty_view)
            views.setPendingIntentTemplate(R.id.list_view, clickPendingIntent)

            val appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId)
            resizeWidget(appWidgetOptions, views)

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        val views = RemoteViews(context.packageName, R.layout.git_favorite_widget)
        resizeWidget(newOptions!!, views)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun resizeWidget(appWidgetOptions: Bundle, views: RemoteViews) {
        val maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

        if (maxHeight > 100) {
            views.setViewVisibility(R.id.list_view, View.VISIBLE)
        } else {
            views.setViewVisibility(R.id.list_view, View.GONE)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context) {
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context) {
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show()
    }

    override fun onReceive(context: Context, intent: Intent) {
        if(ACTION_REFRESH == intent.action) {

            val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)
            val views = RemoteViews(context.packageName, R.layout.git_favorite_widget)
            val appWidgetId: Int = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)

            Toast.makeText(context, "Touched view $appWidgetId", Toast.LENGTH_SHORT).show()
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        super.onReceive(context, intent)
    }
}
