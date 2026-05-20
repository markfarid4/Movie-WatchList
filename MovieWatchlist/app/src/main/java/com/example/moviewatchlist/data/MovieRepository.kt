package com.example.moviewatchlist.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.moviewatchlist.database.MovieContract

class MovieRepository(private val context: Context) {

    fun getAllMovies(): Cursor? {
        return context.contentResolver.query(
            MovieContract.MovieEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        )
    }

    fun getMovie(uri: Uri): Cursor? {
        return context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )
    }

    fun insertMovie(values: ContentValues): Uri? {
        return context.contentResolver.insert(
            MovieContract.MovieEntry.CONTENT_URI,
            values
        )
    }

    fun updateMovie(uri: Uri, values: ContentValues): Int {
        return context.contentResolver.update(
            uri,
            values,
            null,
            null
        )
    }

    fun deleteMovie(uri: Uri): Int {
        return context.contentResolver.delete(
            uri,
            null,
            null
        )
    }
}