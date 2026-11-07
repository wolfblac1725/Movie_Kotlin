package com.erik.canseco.movies.navigation

import android.app.Activity
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.erik.canseco.movies.ads.InterstitialAdManager
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.presentation.screen.actordetail.DetailPersonalScreenRoot
import com.erik.canseco.movies.presentation.screen.home.HomeScreenRoot
import com.erik.canseco.movies.presentation.screen.moviedetail.DetailMovieScreenRoot
import kotlinx.serialization.json.Json

@Composable
fun NavRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current

    DisposableEffect(navController, context) {
        val homeRoute = Home::class.qualifiedName
        val detailRoute = Detail::class.qualifiedName
        var wasMovieDetail = navController.currentDestination?.route
            ?.startsWith(detailRoute.orEmpty()) == true
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route.orEmpty()
            val returnedToMovieList = wasMovieDetail && route == homeRoute
            wasMovieDetail = route.startsWith(detailRoute.orEmpty())
            if (returnedToMovieList) {
                (context as? Activity)?.let(InterstitialAdManager::onQualifyingAction)
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

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
                },
                onDetailPersonal = { actorId ->
                    navController.navigate(DetailPersonal(actorId))
                }
            )
        }
        composable <DetailPersonal> { backStackEntry ->
            val actorId: DetailPersonal = backStackEntry.toRoute()
            val actor = actorId.actorId
            DetailPersonalScreenRoot(
                actorId = actor,
                onBack = {
                    navController.popBackStack()
                }
            )


        }
    }
}