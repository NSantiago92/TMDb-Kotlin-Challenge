package com.nsantiago.tmdbkotlinchallenge.network

import com.nsantiago.tmdbkotlinchallenge.utils.EnvVariables
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TMDbService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val LOW_RES_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342"
        const val HIGH_RES_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780"
    }
    @GET("discover/movie")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") apikey: String = EnvVariables.API_KEY
    ): NetworkMovieContainer

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apikey: String = EnvVariables.API_KEY
    ): NetworkMovieDetail
}

object TMDbNetwork {
    private val retrofit = Retrofit.Builder()
        .baseUrl(TMDbService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val TMDd = retrofit.create(TMDbService::class.java)
}