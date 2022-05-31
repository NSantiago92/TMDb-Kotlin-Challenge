package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.nsantiago.tmdbkotlinchallenge.database.getDatabase
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import com.nsantiago.tmdbkotlinchallenge.utils.serviceModule
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.lang.IllegalArgumentException

class MovieListViewModel(
    application: Application,
    private val moviesRepository: MoviesRepository) : AndroidViewModel(application) {


    private var _searchQuery = ""
    val searchQuery get () = _searchQuery
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