package com.example.moviewatchlist.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.BaseColumns

class MovieProvider : ContentProvider() {

    private lateinit var dbHelper: MovieDbHelper

    override fun onCreate(): Boolean {
        dbHelper = MovieDbHelper(context ?: return false)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val cursor: Cursor = when (uriMatcher.match(uri)) {
            MOVIES -> {
                db.query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }

            MOVIE_ID -> {
                val id = ContentUris.parseId(uri).toString()
                db.query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    projection,
                    "${BaseColumns._ID}=?",
                    arrayOf(id),
                    null,
                    null,
                    sortOrder
                )
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        context?.contentResolver?.let {
            cursor.setNotificationUri(it, uri)
        }

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != MOVIES) {
            throw IllegalArgumentException("Insertion not supported for URI: $uri")
        }

        val db = dbHelper.writableDatabase
        val id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values)

        if (id == -1L) {
            return null
        }

        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase

        val rowsUpdated = when (uriMatcher.match(uri)) {
            MOVIES -> {
                db.update(
                    MovieContract.MovieEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                )
            }

            MOVIE_ID -> {
                val id = ContentUris.parseId(uri).toString()
                db.update(
                    MovieContract.MovieEntry.TABLE_NAME,
                    values,
                    "${BaseColumns._ID}=?",
                    arrayOf(id)
                )
            }

            else -> throw IllegalArgumentException("Update not supported for URI: $uri")
        }

        if (rowsUpdated != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return rowsUpdated
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase

        val rowsDeleted = when (uriMatcher.match(uri)) {
            MOVIES -> {
                db.delete(
                    MovieContract.MovieEntry.TABLE_NAME,
                    selection,
                    selectionArgs
                )
            }

            MOVIE_ID -> {
                val id = ContentUris.parseId(uri).toString()
                db.delete(
                    MovieContract.MovieEntry.TABLE_NAME,
                    "${BaseColumns._ID}=?",
                    arrayOf(id)
                )
            }

            else -> throw IllegalArgumentException("Delete not supported for URI: $uri")
        }

        if (rowsDeleted != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return rowsDeleted
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            MOVIES -> "vnd.android.cursor.dir/${MovieContract.CONTENT_AUTHORITY}/${MovieContract.PATH_MOVIES}"
            MOVIE_ID -> "vnd.android.cursor.item/${MovieContract.CONTENT_AUTHORITY}/${MovieContract.PATH_MOVIES}"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    companion object {
        private const val MOVIES = 100
        private const val MOVIE_ID = 101

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES)
            addURI(MovieContract.CONTENT_AUTHORITY, "${MovieContract.PATH_MOVIES}/#", MOVIE_ID)
        }
    }
}