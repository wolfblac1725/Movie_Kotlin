package com.erik.canseco.movies.details.presentation

import com.erik.canseco.movies.movielist.domain.model.Cast

data class DetailCastState(
    val isLoading: Boolean = false,
    val cast: List<Cast> = emptyList(),
    val error: String? = null
)