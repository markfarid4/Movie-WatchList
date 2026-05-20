package com.example.moviewatchlist.database

import android.net.Uri
import android.provider.BaseColumns

object MovieContract {

    const val CONTENT_AUTHORITY = "com.example.moviewatchlist.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

    const val PATH_MOVIES = "movies"

    object MovieEntry : BaseColumns {

        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIES)
            .build()

        const val TABLE_NAME = "movies"

        const val COLUMN_TITLE = "title"
        const val COLUMN_GENRE = "genre"
        const val COLUMN_YEAR = "year"
        const val COLUMN_STATUS = "status"
        const val COLUMN_RATING = "rating"
    }
}
