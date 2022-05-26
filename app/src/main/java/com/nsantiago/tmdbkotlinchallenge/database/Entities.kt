package com.nsantiago.tmdbkotlinchallenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.domain.MovieDetail
import com.nsantiago.tmdbkotlinchallenge.network.TMDbService

@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String,
    val backdrop_path: String,
    val genres: String,
    val original_language: String,
    val overview: String,
    val popularity: Float,
    val release_date: String,
    val status: String,
    val vote_count: Float,
    val vote_average: Float,
)


fun DatabaseMovie.asDomainModel(): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        posterUrl = TMDbService.LOW_RES_IMAGE_BASE_URL + poster_path,
        backdropUrl = TMDbService.LOW_RES_IMAGE_BASE_URL + backdrop_path,
        genres = genres.split(","),
        originalLanguage = original_language,
        overview = overview,
        popularity = popularity,
        releaseDate = release_date,
        status = status,
        voteCount = vote_count,
        voteAverage = vote_average,
    )
}
