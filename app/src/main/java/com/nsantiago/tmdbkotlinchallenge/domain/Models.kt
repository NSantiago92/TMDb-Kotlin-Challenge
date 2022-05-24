package com.nsantiago.tmdbkotlinchallenge.domain



data class Movie (
    val id: Int,
    val title: String,
    val poster_path: String,
    val genre_ids: List<Int>,
    val popularity: Float,
    val overview: String,
    val release_date: String,
    val adult: Boolean,
    val vote_average: Float,
    val vote_count: Int,
    )

data class Genre (
    val id: Int,
    val name: String,
    )