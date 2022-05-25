package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nsantiago.tmdbkotlinchallenge.database.getDatabase
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val moviesRepository = MoviesRepository(getDatabase(application))
    val movieList = moviesRepository.movieList
    val apiStatus = moviesRepository.apiStatus

    init {
        refreshMovieRepository()
    }

    private fun refreshMovieRepository() {
        viewModelScope.launch {
            moviesRepository.refreshMovieList()
        }

    }

    fun loadNextPage() {
        viewModelScope.launch {
                moviesRepository.loadNextPage()
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}