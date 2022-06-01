package com.nsantiago.tmdbkotlinchallenge.network


import com.nsantiago.tmdbkotlinchallenge.database.DatabaseMovie
import com.nsantiago.tmdbkotlinchallenge.domain.Movie
import com.nsantiago.tmdbkotlinchallenge.domain.MovieDetail
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val results: List<NetworkMovie>)

@JsonClass(generateAdapter = true)
data class NetworkMovie(
    val id: Int,
    val poster_path: String?,
    val title: String,
)

@JsonClass(generateAdapter = true)
data class Genre(
    val name: String,
)

@JsonClass(generateAdapter = true)
data class NetworkMovieDetail(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val genres: List<Genre>,
    val original_language: String,
    val overview: String?,
    val popularity: Float,
    val release_date: String,
    val status: String,
    val vote_count: Int,
    val vote_average: Float,
    val original_title: String,
    )

@JsonClass(generateAdapter = true)
data class RateMovieResponse(
    val status_code: Int,
    val success: Boolean?,
    val status_message: String,
)

@JsonClass(generateAdapter = true)
data class GuestSessionResponse(
    val success: Boolean?,
    val guest_session_id: String?,
    val expires_at: String?,
    val status_message: String?,
    val status_code: Int ?,
)

@JsonClass(generateAdapter = true)
data class RateMovieBody(
    val value:Float,
)

fun NetworkMovieDetail.asDomainModel(): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        posterUrl = TMDbService.LOW_RES_IMAGE_BASE_URL + poster_path,
        backdropUrl = TMDbService.HIGH_RES_IMAGE_BASE_URL + backdrop_path,
        genres = genres.map {it.name},
        originalLanguage = original_language,
        overview = overview?:"",
        popularity = popularity,
        releaseDate = release_date,
        status = status,
        voteCount = vote_count,
        voteAverage = vote_average,
        originalTitle = original_title
    )
}
fun NetworkMovieDetail.asDatabaseModel(): DatabaseMovie {
    return DatabaseMovie(
        id = id,
        title = title,
        original_title = original_title,
        poster_path = poster_path?:"",
        backdrop_path = backdrop_path?:"",
        genres = genres.joinToString(",") { it.name },
        original_language = original_language,
        overview = overview?:"",
        popularity = popularity,
        release_date = release_date,
        status = status,
        vote_count = vote_count,
        vote_average = vote_average,
    )
}
fun NetworkMovieContainer.asDomainModel(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            poster_url = TMDbService.LOW_RES_IMAGE_BASE_URL + it.poster_path,
        )
    }
}
