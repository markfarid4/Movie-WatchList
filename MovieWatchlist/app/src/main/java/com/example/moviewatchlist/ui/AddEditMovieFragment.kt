package com.example.moviewatchlist.ui

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviewatchlist.R
import com.example.moviewatchlist.customview.RatingTextView
import com.example.moviewatchlist.database.MovieContract
import com.example.moviewatchlist.viewmodel.MoviesViewModel

class AddEditMovieFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private var movieUri: Uri? = null

    private lateinit var editTitle: EditText
    private lateinit var editGenre: EditText
    private lateinit var editYear: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var seekRating: SeekBar
    private lateinit var ratingPreview: RatingTextView
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]

        arguments?.getString("uri")?.let {
            movieUri = Uri.parse(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_edit_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editTitle = view.findViewById(R.id.editTitle)
        editGenre = view.findViewById(R.id.editGenre)
        editYear = view.findViewById(R.id.editYear)
        radioGroup = view.findViewById(R.id.radioGroupStatus)
        seekRating = view.findViewById(R.id.seekRating)
        ratingPreview = view.findViewById(R.id.textRatingPreview)
        buttonSave = view.findViewById(R.id.buttonSave)
        buttonDelete = view.findViewById(R.id.buttonDelete)

        seekRating.progress = 0
        ratingPreview.setRating(1)
        radioGroup.check(R.id.radioPlanned)

        seekRating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ratingPreview.setRating(progress + 1)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        if (movieUri != null) {
            buttonDelete.visibility = View.VISIBLE

            viewModel.selectedMovie.observe(viewLifecycleOwner) { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    fillFields(cursor)
                }
            }

            viewModel.getMovieLiveData(movieUri!!)
        }

        buttonSave.setOnClickListener {
            saveMovie()
        }

        buttonDelete.setOnClickListener {
            movieUri?.let {
                viewModel.deleteMovie(it)
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun fillFields(cursor: Cursor) {
        editTitle.setText(
            cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE))
        )
        editGenre.setText(
            cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_GENRE))
        )
        editYear.setText(
            cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_YEAR)).toString()
        )

        val status = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_STATUS))
        if (status == 1) {
            radioGroup.check(R.id.radioWatched)
        } else {
            radioGroup.check(R.id.radioPlanned)
        }

        val rating = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RATING))
        seekRating.progress = rating - 1
        ratingPreview.setRating(rating)
    }

    private fun saveMovie() {
        val title = editTitle.text.toString().trim()
        val genre = editGenre.text.toString().trim()
        val yearText = editYear.text.toString().trim()

        if (title.isEmpty()) {
            editTitle.error = "Title required"
            return
        }

        val year = yearText.toIntOrNull()
        if (year == null || year < 1900 || year > 2100) {
            editYear.error = "Year must be 1900-2100"
            return
        }

        val status = if (radioGroup.checkedRadioButtonId == R.id.radioWatched) 1 else 0
        val rating = seekRating.progress + 1

        val values = ContentValues().apply {
            put(MovieContract.MovieEntry.COLUMN_TITLE, title)
            put(MovieContract.MovieEntry.COLUMN_GENRE, genre)
            put(MovieContract.MovieEntry.COLUMN_YEAR, year)
            put(MovieContract.MovieEntry.COLUMN_STATUS, status)
            put(MovieContract.MovieEntry.COLUMN_RATING, rating)
        }

        if (movieUri == null) {
            viewModel.insertMovie(values)
        } else {
            viewModel.updateMovie(movieUri!!, values)
        }

        parentFragmentManager.popBackStack()
    }

    companion object {
        fun newInstance(uri: String): AddEditMovieFragment {
            val fragment = AddEditMovieFragment()
            val args = Bundle()
            args.putString("uri", uri)
            fragment.arguments = args
            return fragment
        }
    }
}