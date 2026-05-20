package com.example.moviewatchlist.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RatingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    fun setRating(rating: Int) {
        val safeRating = rating.coerceIn(1, 5)
        text = buildString {
            repeat(safeRating) { append("★") }
            repeat(5 - safeRating) { append("☆") }
        }
    }
}