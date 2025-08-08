package com.erik.canseco.movies.presentation.screen.moviedetail

import com.erik.canseco.movies.domain.model.Cast

data class DetailCastState(
    val isLoading: Boolean = false,
    val cast: List<Cast> = emptyList(),
    val error: String? = null
)