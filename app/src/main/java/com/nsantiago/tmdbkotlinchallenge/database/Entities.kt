package com.nsantiago.tmdbkotlinchallenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nsantiago.tmdbkotlinchallenge.domain.Genre
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.network.TMDbService

@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String,
   // val genre_ids: List<Int>,
    val popularity: Float,
    val overview: String,
    val release_date: String,
    val adult: Boolean,
    val vote_average: Float,
    val vote_count: Int,
)

@Entity
data class DatabaseGenre constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
)


//TODO: add type converter for genres
fun List<DatabaseMovie>.asMovieModel(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            title = it.title,
            poster_url = TMDbService.IMAGE_BASE_URL + it.poster_path,
            genre_ids = listOf(1,2),
            popularity = it.popularity,
            overview = it.overview,
            release_date = it.release_date,
            adult = it.adult,
            vote_average = it.vote_average,
            vote_count = it.vote_count,
        )
    }
}

fun List<DatabaseGenre>.asGenreModel(): List<Genre> {
    return map {
        Genre(
            id = it.id,
            name = it.name,
        )
    }
}