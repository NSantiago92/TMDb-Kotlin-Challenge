package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.launch

class MovieListViewModel(
    application: Application,
    private val moviesRepository: MoviesRepository
) : AndroidViewModel(application) {

    private var _searchQuery = ""
    val searchQuery get() = _searchQuery
    private val fullMovieList = moviesRepository.movieList
    var movieList = MediatorLiveData<List<Movie>>()
    val apiStatus = moviesRepository.apiStatus

    init {
        loadMovies()
        movieList.addSource(moviesRepository.movieList) {
            movieList.value = it.filter {
                it.title.contains(_searchQuery, true)
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            moviesRepository.refreshMovieList()
        }
    }

    fun refreshMovies() {
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
            _searchQuery = query
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            moviesRepository.loadNextPage()
        }
    }

}