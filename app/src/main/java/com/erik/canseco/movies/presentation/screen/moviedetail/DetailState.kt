package com.erik.canseco.movies.presentation.screen.moviedetail

import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie

data class DetailState (
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null,
    val cast: List<Cast> = emptyList()
)