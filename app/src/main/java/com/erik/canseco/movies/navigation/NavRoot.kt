package com.erik.canseco.movies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.presentation.screen.home.HomeScreenRoot
import com.erik.canseco.movies.presentation.screen.moviedetail.DetailMovieScreenRoot
import kotlinx.serialization.json.Json

@Composable
fun NavRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Home
    ){
        composable<Home> {
            HomeScreenRoot(
                modifier,
                onNavigate = { movieId ->
                    navController.navigate(Detail(movieId))
                }
            )
        }
        composable<Detail> { backStackEntry ->
            val movieIndex: Detail  = backStackEntry.toRoute()
            val movieJson = movieIndex.movie
            val json = Json { ignoreUnknownKeys = true }
            val movie = json.decodeFromString<Movie>(movieJson)
            DetailMovieScreenRoot(
                movie = movie,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}