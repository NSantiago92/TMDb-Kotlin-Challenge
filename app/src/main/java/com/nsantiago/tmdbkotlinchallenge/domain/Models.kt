package com.nsantiago.tmdbkotlinchallenge.domain



data class Movie (
    val id: Int,
    val title: String,
    val poster_url: String,
    )
data class MovieDetail (
    val id: Int,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val genres: List<String>,
    val originalLanguage: String,
    val overview: String,
    val popularity: Float,
    val releaseDate: String,
    val status: String,
    val voteCount: Float,
    val voteAverage: Float,
)
