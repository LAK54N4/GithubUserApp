package com.laksana.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.laksana.githubuser.database.DatabaseContract.AUTHORITY
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.laksana.githubuser.database.UserHelper

class MyContentProvider : ContentProvider() {

    companion object {
        private const val GIT = 1
        private const val GIT_ID = 2

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: UserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, GIT)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", GIT_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = UserHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            GIT -> favoriteHelper.queryAll()
            GIT_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (GIT) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (GIT_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (GIT_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}