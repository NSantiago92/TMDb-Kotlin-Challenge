package com.nsantiago.tmdbkotlinchallenge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nsantiago.tmdbkotlinchallenge.database.MoviesDatabase
import com.nsantiago.tmdbkotlinchallenge.database.asDomainModel
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.domain.MovieDetail
import com.nsantiago.tmdbkotlinchallenge.network.TMDbNetwork
import com.nsantiago.tmdbkotlinchallenge.network.asDatabaseModel
import com.nsantiago.tmdbkotlinchallenge.network.asDomainModel
import com.nsantiago.tmdbkotlinchallenge.utils.notifyObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


enum class TMDbApiStatus { LOADING, REFRESHING, DONE, ERROR }

class MoviesRepository(private val database: MoviesDatabase) {

    val movieList = MutableLiveData<MutableList<Movie>>()
    var movieDetail = MutableLiveData<MovieDetail>()

    private var page = 1
    private val _apiStatus = MutableLiveData<TMDbApiStatus>()
    val apiStatus: LiveData<TMDbApiStatus> = _apiStatus

    suspend fun loadMovieDetail(id: Int) {
        val databaseMovie = loadMovieDetailFromDb(id)
        if (databaseMovie != null) {
            movieDetail.value = databaseMovie!!
        } else {
            val networkMovie = loadMovieFromNetwork(id)
            networkMovie?.let { movieDetail.value = it }
        }
    }

    private suspend fun loadMovieDetailFromDb(id: Int): MovieDetail? {
        return withContext(Dispatchers.IO) {
            database.movieDao.getMovie(id)?.asDomainModel()
        }
    }

    private suspend fun loadMovieFromNetwork(id: Int): MovieDetail? {
        _apiStatus.value = TMDbApiStatus.LOADING
        return withContext(Dispatchers.IO) {
            try {
                val movie = TMDbNetwork.TMDd.getMovieDetail(id)
                _apiStatus.postValue(TMDbApiStatus.DONE)
                database.movieDao.insertMovie(movie.asDatabaseModel())
                return@withContext movie.asDomainModel()
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
                return@withContext null
            }
        }
    }

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
                movieList.notifyObserver()
                _apiStatus.postValue(TMDbApiStatus.DONE)
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
            }
        }
    }

}