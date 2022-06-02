package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    application: Application,
    private val moviesRepository: MoviesRepository) : AndroidViewModel(application) {

    val movieDetail = moviesRepository.movieDetail
    val apiStatus = moviesRepository.apiStatus

    fun loadMovieDetail(id: Int) = viewModelScope.launch {
            moviesRepository.loadMovieDetail(id)
    }

    fun refreshMovieDetail() = viewModelScope.launch {
            moviesRepository.refreshMovieDetail()
    }

    fun clearMovieDetail() = moviesRepository.clearMovieDetail()

    fun rateMovie(id:Int, rating: Float): LiveData<Boolean> {
        return liveData {
            emit(moviesRepository.rateMovie(id, rating))
        }
    }


}