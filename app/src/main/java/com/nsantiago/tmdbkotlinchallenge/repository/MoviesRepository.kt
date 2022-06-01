package com.nsantiago.tmdbkotlinchallenge.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nsantiago.tmdbkotlinchallenge.database.MoviesDatabase
import com.nsantiago.tmdbkotlinchallenge.database.asDomainModel
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.domain.MovieDetail
import com.nsantiago.tmdbkotlinchallenge.common.notifyObserver
import com.nsantiago.tmdbkotlinchallenge.network.*
import com.nsantiago.tmdbkotlinchallenge.sharedpreference.SharedPreferenceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


enum class TMDbApiStatus { LOADING, REFRESHING, DONE, ERROR }

class MoviesRepository(
    private val database: MoviesDatabase,
    private val api: TMDbService,
    private val sharedPreferences: SharedPreferenceService,
) {

    val movieList = MutableLiveData<MutableList<Movie>>()
    var movieDetail = MutableLiveData<MovieDetail>()
    private var sessionId: String? = null
    private val emptyMovie = MovieDetail(
        id = -1,
        title = "",
        originalTitle = "",
        posterUrl = "",
        backdropUrl = "",
        genres = listOf(""),
        originalLanguage = "",
        overview = "",
        popularity = 0f,
        releaseDate = "",
        status = "",
        voteCount = 0,
        voteAverage = 0f,
    )
    private var _page = 1
    private val _apiStatus = MutableLiveData<TMDbApiStatus>()
    private var _currentDetailId = -1
    val apiStatus: LiveData<TMDbApiStatus> = _apiStatus

    suspend fun loadMovieDetail(id: Int) {
        _apiStatus.value = TMDbApiStatus.LOADING
        _currentDetailId = id
        val databaseMovie = loadMovieDetailFromDb(id)
        databaseMovie?.let {
            movieDetail.value = it
            _apiStatus.value = TMDbApiStatus.DONE
            return
        }
        val networkMovie = loadMovieDetailFromNetwork(id)
        networkMovie?.let {
            movieDetail.value = it
            _apiStatus.value = TMDbApiStatus.DONE
            return
        }
        _apiStatus.value = TMDbApiStatus.ERROR

    }

    private suspend fun loadMovieDetailFromDb(id: Int): MovieDetail? {
        return withContext(Dispatchers.IO) {
            database.movieDao.getMovie(id)?.asDomainModel()
        }
    }

    private suspend fun loadMovieDetailFromNetwork(id: Int): MovieDetail? {
        _apiStatus.value = TMDbApiStatus.LOADING
        return withContext(Dispatchers.IO) {
            try {
                val movie = api.getMovieDetail(id)
                _apiStatus.postValue(TMDbApiStatus.DONE)
                database.movieDao.insertMovie(movie.asDatabaseModel())
                return@withContext movie.asDomainModel()
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
                return@withContext null
            }
        }
    }

    suspend fun refreshMovieDetail() {
        loadMovieDetailFromNetwork(_currentDetailId)?.let {
            movieDetail.value = it
        }
    }

    fun clearMovieDetail() {
        movieDetail.value = emptyMovie
    }

    suspend fun refreshMovieList() {
        _apiStatus.value = TMDbApiStatus.LOADING
        _page = 1
        withContext(Dispatchers.IO) {
            try {
                movieList.postValue(
                    api.getPopularMovies(_page).asDomainModel().toMutableList()
                )
                _apiStatus.postValue(TMDbApiStatus.DONE)
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
            }
        }
    }

    suspend fun loadNextPage() {
        _apiStatus.value = TMDbApiStatus.REFRESHING
        _page += 1
        withContext(Dispatchers.IO) {
            try {
                movieList.value?.addAll(api.getPopularMovies(_page).asDomainModel())
                movieList.notifyObserver()
                _apiStatus.postValue(TMDbApiStatus.DONE)
            } catch (networkError: IOException) {
                _apiStatus.postValue(TMDbApiStatus.ERROR)
            }
        }
    }

    suspend fun rateMovie(id: Int, rating: Float): Boolean {
        if (sessionId == null) sessionId = getSessionId()
        if (sessionId == null) {
            return false
        }
        return try {
            val postStatus =
                api.postMovieRating(
                    movie_id = id,
                    guest_session_id = sessionId!!,
                    body = RateMovieBody(rating),
                ).success
            postStatus == true
        } catch (networkError: IOException) {
            sharedPreferences.removeGuestSessionId()
            sessionId = null
            false
        }
    }

    private suspend fun getSessionId(): String? {
        var newSession = sharedPreferences.getGuestSessionId()
        if (newSession.isNullOrEmpty()) {
            newSession = withContext(Dispatchers.IO) {
                try {
                    val networkSession = api.getGuestSession().guest_session_id
                    networkSession?.let { sharedPreferences.putGuestSessionId(it) }
                    return@withContext networkSession
                } catch (networkError: IOException) {
                    return@withContext null
                }
            }
        }
        return newSession
    }

    private suspend fun refreshSessionId(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val networkSession = api.getGuestSession().guest_session_id
                networkSession?.let { sharedPreferences.putGuestSessionId(it) }
                return@withContext networkSession
            } catch (networkError: IOException) {
                return@withContext null
            }
        }
    }
}