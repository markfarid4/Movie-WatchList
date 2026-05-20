package com.example.moviewatchlist.ui

import android.database.Cursor
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.customview.RatingTextView
import com.example.moviewatchlist.database.MovieContract

class MovieAdapter(
    private var cursor: Cursor?,
    private val onItemClick: (Long) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val genreYear: TextView = itemView.findViewById(R.id.textGenreYear)
        val status: TextView = itemView.findViewById(R.id.textStatus)
        val rating: RatingTextView = itemView.findViewById(R.id.textRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentCursor = cursor ?: return
        if (!currentCursor.moveToPosition(position)) return

        val id = currentCursor.getLong(
            currentCursor.getColumnIndexOrThrow(BaseColumns._ID)
        )

        val title = currentCursor.getString(
            currentCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE)
        )

        val genre = currentCursor.getString(
            currentCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_GENRE)
        )

        val year = currentCursor.getInt(
            currentCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_YEAR)
        )

        val movieStatus = currentCursor.getInt(
            currentCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_STATUS)
        )

        val movieRating = currentCursor.getInt(
            currentCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RATING)
        )

        holder.title.text = title
        holder.genreYear.text = "$genre ($year)"
        holder.status.text = if (movieStatus == 1) "Watched" else "Planned"
        holder.rating.setRating(movieRating)

        holder.itemView.setOnClickListener {
            onItemClick(id)
        }
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    fun swapCursor(newCursor: Cursor?) {
        cursor = newCursor
        notifyDataSetChanged()
    }
}