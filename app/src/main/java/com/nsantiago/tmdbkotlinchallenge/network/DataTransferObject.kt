package com.nsantiago.tmdbkotlinchallenge.network

import com.nsantiago.tmdbkotlinchallenge.domain.Genre
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val results: List<NetworkMovie>)

@JsonClass(generateAdapter = true)
data class NetworkMovie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int,
)

@JsonClass(generateAdapter = true)
data class NetworkGenreContainer(val genres: List<NetworkGenre>)

@JsonClass(generateAdapter = true)
data class NetworkGenre(
    val id: Int,
    val name: String,
)

fun NetworkMovieContainer.asDomainModel(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            poster_path = it.poster_path,
            genre_ids = it.genre_ids,
            popularity = it.popularity,
            overview = it.overview,
            release_date = it.release_date,
            adult = it.adult,
            vote_average = it.vote_average,
            vote_count = it.vote_count,
        )
    }
}

fun NetworkGenreContainer.asDomainModel(): List<Genre> {
    return genres.map {
        Genre(
            id = it.id,
            name = it.name,
        )
    }
}