package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nsantiago.tmdbkotlinchallenge.database.getDatabase
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    //TODO: esto hay que inyectar
    private val moviesRepository = MoviesRepository(getDatabase(application))
    val movieDetail = moviesRepository.movieDetail
    val apiStatus = moviesRepository.apiStatus

    fun loadMovieDetail(id: Int) {
        viewModelScope.launch {
            moviesRepository.loadMovieDetail(id)
        }
    }



    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}