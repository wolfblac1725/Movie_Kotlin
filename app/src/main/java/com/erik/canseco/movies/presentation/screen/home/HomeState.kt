package com.erik.canseco.movies.presentation.screen.home

import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.utility.TypeShared

data class HomeState(
    val isLoading: Boolean = false,
    val typeShared: TypeShared = TypeShared.Popular,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)
