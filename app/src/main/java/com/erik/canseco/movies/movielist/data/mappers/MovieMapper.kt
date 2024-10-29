package com.erik.canseco.movies.movielist.data.mappers

import com.erik.canseco.movies.movielist.data.local.movie.MovieEntity
import com.erik.canseco.movies.movielist.data.remote.response.MovieDto
import com.erik.canseco.movies.movielist.domain.model.Movie

fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        video = video ?: false,
        id = id ?: -1,
        original_title = original_title ?: "",
        category = category,
    )
}

fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        video = video,
        id = id,
        adult = adult,
        original_title = original_title,
        category = category,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1,-2)
        },
    )
}