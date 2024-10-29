package com.erik.canseco.movies.core.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erik.canseco.movies.core.presentation.PopularMoviesScreen
import com.erik.canseco.movies.core.presentation.UpcomingMoviesScreen
import com.erik.canseco.movies.movielist.domain.util.Screen
import com.erik.canseco.movies.movielist.presentation.MovieListState
import com.erik.canseco.movies.movielist.presentation.MovieListViewModel

@Composable
fun BottomNavHost(
    bottomNavController: NavHostController,
    navController: NavHostController,
    route: String,movieListState: MovieListState,
    model: MovieListViewModel
) {
    NavHost(
        navController = bottomNavController,
        startDestination = route
    ) {
        composable(Screen.PopularMovies.route) {
            PopularMoviesScreen(
                navController = navController,
                movieListState = movieListState,
                onEvent = model::onEvent
            )
        }
        composable(Screen.UpcomingMovies.route) {
            UpcomingMoviesScreen(
                navController = navController,
                movieListState = movieListState,
                onEvent = model::onEvent
            )
        }

    }
}