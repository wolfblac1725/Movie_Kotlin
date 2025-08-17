package com.erik.canseco.movies.presentation.screen.home

import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.utility.TypeShared

data class HomeState(
    val typeShared: TypeShared = TypeShared.Popular,
    val moviesPopular: MovilPage = MovilPage(),
    val moviesTopRated: MovilPage = MovilPage(),
    val moviesNowPlaying: MovilPage = MovilPage(),
)
data class MovilPage(
    val page: Int = 1,
    val movies: List<Movie> = emptyList(),
    val totalPage: Int = 1,
    val error: String? = null,
    val isLoading: Boolean = false,
)
