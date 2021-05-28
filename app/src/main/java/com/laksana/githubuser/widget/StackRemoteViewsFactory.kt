package com.laksana.githubuser.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.laksana.githubuser.R
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import java.util.concurrent.ExecutionException

internal class StackRemoteViewsFactory (private val mContext: Context, intent: Intent) :
        RemoteViewsService.RemoteViewsFactory {

    private var cursor: Cursor? = null

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor?.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
        Log.d("cursor: ", cursor.toString())
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return cursor?.count ?: 0
    }

    private fun getItem(position: Int): ItemFavorites {
        cursor?.moveToPosition(position)?.let { check(it) { "Position invalid!" } }
        return ItemFavorites(cursor)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val item: ItemFavorites = getItem(position)
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        var bitmap: Bitmap? = null
        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(item.avatar)
                    .submit()
                    .get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        rv.setImageViewBitmap(R.id.widgetAvatar, bitmap)
        rv.setTextViewText(R.id.widgetUsername, item.username )

        val extras = Bundle()
        extras.putString(GitFavoriteWidget.EXTRA_ITEM_POSITION, item.username)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.widgetUsername, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}
