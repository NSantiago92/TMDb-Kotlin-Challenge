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

class MovieDetailViewModel(
    application: Application,
    private val moviesRepository: MoviesRepository) : AndroidViewModel(application) {

    val movieDetail = moviesRepository.movieDetail
    val apiStatus = moviesRepository.apiStatus

    fun loadMovieDetail(id: Int) {
        viewModelScope.launch {
            moviesRepository.loadMovieDetail(id)
        }
    }


}