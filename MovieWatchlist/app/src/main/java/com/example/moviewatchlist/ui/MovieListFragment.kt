package com.example.moviewatchlist.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.database.MovieContract
import com.example.moviewatchlist.viewmodel.MoviesViewModel

class MovieListFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var textEmpty: TextView
    private lateinit var buttonAddMovie: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerViewMovies)
        textEmpty = view.findViewById(R.id.textEmpty)
        buttonAddMovie = view.findViewById(R.id.buttonAddMovie)

        adapter = MovieAdapter(null) { movieId ->
            val uri = Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI, movieId.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddEditMovieFragment.newInstance(uri.toString()))
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        buttonAddMovie.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddEditMovieFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.movies.observe(viewLifecycleOwner) { cursor ->
            adapter.swapCursor(cursor)
            textEmpty.visibility = if (cursor == null || cursor.count == 0) View.VISIBLE else View.GONE
        }

        viewModel.loadMovies()
    }
}