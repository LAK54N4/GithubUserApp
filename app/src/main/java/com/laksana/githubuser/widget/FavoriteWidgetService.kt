package com.laksana.githubuser.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, intent)
    }
}
