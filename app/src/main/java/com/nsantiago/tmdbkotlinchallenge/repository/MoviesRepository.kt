package com.nsantiago.tmdbkotlinchallenge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nsantiago.tmdbkotlinchallenge.database.MoviesDatabase
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.network.TMDbNetwork
import com.nsantiago.tmdbkotlinchallenge.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


enum class TMDbApiStatus { LOADING, REFRESHING, DONE, ERROR }

class MoviesRepository(private val database: MoviesDatabase) {
    //TODO: movieDetail
    //TODO: Fallback on cached movies to populate the list
    val movieList = MutableLiveData<MutableList<Movie>>()
    private var page = 1
    private val _apiStatus = MutableLiveData<TMDbApiStatus>()
    val apiStatus: LiveData<TMDbApiStatus> = _apiStatus

    suspend fun refreshMovieList() {
        _apiStatus.value = TMDbApiStatus.LOADING
        withContext(Dispatchers.IO) {
            try {
                movieList.postValue(
                    TMDbNetwork.TMDd.getPopularMovies(1).asDomainModel().toMutableList()
                )
                _apiStatus.postValue(TMDbApiStatus.DONE)
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
            }
        }
    }

    suspend fun loadNextPage() {
        _apiStatus.value = TMDbApiStatus.REFRESHING
        page += 1
        withContext(Dispatchers.IO) {
            try {
                movieList.value?.addAll(TMDbNetwork.TMDd.getPopularMovies(page).asDomainModel())
                Log.d("REPOSITORY", "LOADED NEW PAGES ${movieList.value?.size}")
                _apiStatus.postValue(TMDbApiStatus.DONE)
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
            }
        }
    }

}