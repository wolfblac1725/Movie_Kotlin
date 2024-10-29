package com.erik.canseco.movies.details.presentation

import com.erik.canseco.movies.movielist.domain.model.Movie

data class DetailState (
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null
)