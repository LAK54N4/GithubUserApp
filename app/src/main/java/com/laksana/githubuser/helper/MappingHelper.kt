package com.laksana.githubuser.helper

import android.database.Cursor
import com.laksana.githubuser.database.DatabaseContract
import com.laksana.githubuser.model.UserFavorite

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<UserFavorite> {
        val favoriteList = ArrayList<UserFavorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                favoriteList.add(UserFavorite(id, username, avatar))
            }
        }
        return favoriteList
    }
}