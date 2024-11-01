package com.erik.canseco.movies.core.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erik.canseco.movies.core.presentation.PopularMoviesPagerScreen
import com.erik.canseco.movies.core.presentation.UpcomingMoviesScreen
import com.erik.canseco.movies.movielist.domain.util.Screen
import com.erik.canseco.movies.movielist.presentation.MovieListViewModel

@Composable
fun BottomNavHost(
    bottomNavController: NavHostController,
    navController: NavHostController,
    route: String,
    model: MovieListViewModel
) {
    NavHost(
        navController = bottomNavController,
        startDestination = route
    ) {
        composable(Screen.PopularMovies.route) {
            PopularMoviesPagerScreen(
                navController = navController,
                model = model
            )

        }
        composable(Screen.UpcomingMovies.route) {
            UpcomingMoviesScreen(
                navController = navController,
                model = model
            )
        }

    }
}