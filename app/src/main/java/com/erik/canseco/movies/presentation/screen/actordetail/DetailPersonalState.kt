package com.erik.canseco.movies.presentation.screen.actordetail

import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.model.Person

data class DetailPersonalState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val actor: Person? = null,
    val movies: List<Movie>? = null
)