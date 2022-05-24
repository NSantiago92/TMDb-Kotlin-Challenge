package com.nsantiago.tmdbkotlinchallenge.repository

import androidx.lifecycle.MutableLiveData
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.network.TMDbNetwork
import com.nsantiago.tmdbkotlinchallenge.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(/*private val database: MoviesDatabase*/) {
    //TODO: refreshMovie(id)
    //TODO: movies
    //TODO: movieDetail
    val movieList = MutableLiveData<MutableList<Movie>>()
    private var page = 1
    suspend fun refreshMovieList() {
        withContext(Dispatchers.IO) {
            movieList.postValue(TMDbNetwork.TMDd.getPopularMovies(1).asDomainModel().toMutableList())
        }
    }
    suspend fun loadNextPage() {
        page += 1
        withContext(Dispatchers.IO) {
            movieList.value?.addAll(TMDbNetwork.TMDd.getPopularMovies(page).asDomainModel())
        }
    }

}