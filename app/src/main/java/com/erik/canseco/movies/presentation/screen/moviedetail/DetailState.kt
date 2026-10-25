package com.erik.canseco.movies.presentation.screen.moviedetail

import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Genre
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.model.MovieVideo

data class DetailState (
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null,
    val cast: List<Cast> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val trailers: List<MovieVideo> = emptyList(),
    val isLoadingTrailers: Boolean = false,
    val trailersLoaded: Boolean = false,
    val trailersError: String? = null
)