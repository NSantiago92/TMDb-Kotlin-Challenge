package com.nsantiago.tmdbkotlinchallenge.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "b83c436ea678e7b94f949f696bc2f40d"
    }
    @GET("https://api.themoviedb.org/3/discover/movie?api_key=b83c436ea678e7b94f949f696bc2f40d")
    suspend fun getPopularMovies(@Query("page") page: Int): NetworkMovieContainer
}

object TMDbNetwork {
    private val retrofit = Retrofit.Builder()
        .baseUrl(TMDbService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val TMDd = retrofit.create(TMDbService::class.java)
}