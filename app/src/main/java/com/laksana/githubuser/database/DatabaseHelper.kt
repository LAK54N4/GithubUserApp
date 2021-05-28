package com.laksana.githubuser.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion._ID

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "dbUser"

        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_NAME_USERNAME TEXT NOT NULL," +
                " $COLUMN_NAME_AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}

