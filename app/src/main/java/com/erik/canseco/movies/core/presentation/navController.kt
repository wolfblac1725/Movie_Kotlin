package com.erik.canseco.movies.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.erik.canseco.movies.details.presentation.DetailScreen
import com.erik.canseco.movies.movielist.domain.util.Screen

@Composable
fun NavController() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route){
            HomeScreen(navController)
        }
        composable(
            route = Screen.Details.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ){
            DetailScreen()
        }
    }
}

