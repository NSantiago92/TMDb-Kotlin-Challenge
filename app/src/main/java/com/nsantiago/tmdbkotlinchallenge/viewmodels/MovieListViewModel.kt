package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val moviesRepository = MoviesRepository(/*getDatabase(application)*/)
    val movieList = moviesRepository.movieList

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    private val _eventNetworkErrorShown = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    val eventNetworkErrorShown: LiveData<Boolean>
        get() = _eventNetworkErrorShown

    init {
        refreshMovieRepository()
    }

    private fun refreshMovieRepository() {
        viewModelScope.launch {
            try {
                moviesRepository.refreshMovieList()
                _eventNetworkError.value = false
                _eventNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            try {
                moviesRepository.loadNextPage()
            } catch (networkError: IOException) {

            }
        }
    }

    fun onNetworkErrorShown() {
        _eventNetworkErrorShown.value = true
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