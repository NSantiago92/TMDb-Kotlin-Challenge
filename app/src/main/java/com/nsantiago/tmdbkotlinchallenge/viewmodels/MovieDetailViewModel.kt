package com.nsantiago.tmdbkotlinchallenge.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nsantiago.tmdbkotlinchallenge.database.getDatabase
import com.nsantiago.tmdbkotlinchallenge.repository.MoviesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

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