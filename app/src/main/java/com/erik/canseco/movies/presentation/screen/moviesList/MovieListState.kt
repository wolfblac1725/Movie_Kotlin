package com.erik.canseco.movies.presentation.screen.moviesList

import com.erik.canseco.movies.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val moviesPopular: List<Movie> = emptyList(),
    val moviesUpcoming: List<Movie> = emptyList(),

    val isCurrentPopularScreen: Boolean = true,

    val popularPage: Int = 1,
    val upcomingPage: Int = 1

)
