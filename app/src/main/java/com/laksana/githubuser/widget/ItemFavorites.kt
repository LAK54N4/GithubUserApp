package com.laksana.githubuser.widget


import android.database.Cursor
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion._ID

class ItemFavorites (cursor: Cursor?) {
    var id: Int = 0
    var username: String? = null
    var avatar: String? = null

    init {
        id = cursor!!.getInt(cursor.getColumnIndex(_ID))
        username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME))
        avatar = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVATAR_URL))
    }

    override fun toString(): String {
        return """ItemFavorites{id='$id', username = '$username', 
            |avatar = '$avatar'}
        """.trimMargin()
    }
}

