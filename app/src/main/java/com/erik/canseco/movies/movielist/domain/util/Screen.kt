package com.erik.canseco.movies.movielist.domain.util

sealed class Screen (val route: String) {
    object Home : Screen("home")
    object PopularMovies : Screen("popularMovies")
    object UpcomingMovies : Screen("upcomingMovies")
    object Details : Screen("details")
}