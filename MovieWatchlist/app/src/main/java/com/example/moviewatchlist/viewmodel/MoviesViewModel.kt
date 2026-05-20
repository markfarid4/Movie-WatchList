package com.example.moviewatchlist.viewmodel

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviewatchlist.data.MovieRepository

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application)

    private val _movies = MutableLiveData<Cursor?>()
    val movies: LiveData<Cursor?> = _movies

    private val _selectedMovie = MutableLiveData<Cursor?>()
    val selectedMovie: LiveData<Cursor?> = _selectedMovie

    fun loadMovies() {
        _movies.value = repository.getAllMovies()
    }

    fun getMovieLiveData(uri: Uri) {
        _selectedMovie.value = repository.getMovie(uri)
    }

    fun insertMovie(values: ContentValues) {
        repository.insertMovie(values)
        loadMovies()
    }

    fun updateMovie(uri: Uri, values: ContentValues) {
        repository.updateMovie(uri, values)
        loadMovies()
    }

    fun deleteMovie(uri: Uri) {
        repository.deleteMovie(uri)
        loadMovies()
    }
}