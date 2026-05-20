package com.example.moviewatchlist.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE ${MovieContract.MovieEntry.TABLE_NAME} (
                ${android.provider.BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${MovieContract.MovieEntry.COLUMN_TITLE} TEXT NOT NULL,
                ${MovieContract.MovieEntry.COLUMN_GENRE} TEXT,
                ${MovieContract.MovieEntry.COLUMN_YEAR} INTEGER,
                ${MovieContract.MovieEntry.COLUMN_STATUS} INTEGER NOT NULL DEFAULT 0,
                ${MovieContract.MovieEntry.COLUMN_RATING} INTEGER NOT NULL DEFAULT 1
            )
        """.trimIndent()

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${MovieContract.MovieEntry.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 1
    }
}