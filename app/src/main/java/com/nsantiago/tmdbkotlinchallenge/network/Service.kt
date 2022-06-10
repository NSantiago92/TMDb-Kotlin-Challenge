package com.nsantiago.tmdbkotlinchallenge.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


interface TMDbService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val LOW_RES_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342"
        const val HIGH_RES_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780"
        const val API_KEY = "b83c436ea678e7b94f949f696bc2f40d"
    }

    @GET("discover/movie")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") apikey: String = API_KEY,
    ): NetworkMovieContainer

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apikey: String = API_KEY,
    ): NetworkMovieDetail

    @GET("authentication/guest_session/new")
    suspend fun getGuestSession(
        @Query("api_key") apikey: String = API_KEY,
    ):GuestSessionResponse

    @Headers("Content-Type: application/json")
    @POST("movie/{movie_id}/rating")
    suspend fun postMovieRating(
        @Path("movie_id") movie_id: Int,
        @Query("guest_session_id") guest_session_id: String,
        @Body body: RateMovieBody,
        @Query("api_key") apikey: String = API_KEY,
    ):RateMovieResponse
}

fun getTMDbService(): TMDbService {
    return Retrofit.Builder()
        .baseUrl(TMDbService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(TMDbService::class.java)
}