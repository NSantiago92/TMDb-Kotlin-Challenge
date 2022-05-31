package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.nsantiago.tmdbkotlinchallenge.database.getDatabase
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: esto hay que inyectar
    private val moviesRepository = MoviesRepository(getDatabase(application))
    private var searchQuery = ""
    private val fullMovieList = moviesRepository.movieList
    var movieList = MediatorLiveData<List<Movie>>()
    val apiStatus = moviesRepository.apiStatus

    init {
        loadMovies()
        movieList.addSource(moviesRepository.movieList) {
            movieList.value = it.filter {
                it.title.contains(searchQuery, true)
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            moviesRepository.refreshMovieList()
        }

    }

    fun setSearchQuery(query: String) {
        fullMovieList.value.let {
            movieList.value = it?.filter { m ->
                    m.title.contains(query, true)
                }
            }.also {
                searchQuery = query
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