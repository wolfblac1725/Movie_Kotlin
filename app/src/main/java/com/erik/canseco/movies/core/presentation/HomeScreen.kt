package com.erik.canseco.movies.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.erik.canseco.movies.R
import com.erik.canseco.movies.core.component.BottomNavHost
import com.erik.canseco.movies.core.component.BottomNavigationBar
import com.erik.canseco.movies.movielist.domain.util.Screen
import com.erik.canseco.movies.movielist.presentation.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    movieListViewModel: MovieListViewModel = hiltViewModel()
) {
    val movieState = movieListViewModel.moviesListState.collectAsState().value
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = bottomNavController,
                onEvent = movieListViewModel::onEvent
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (movieState.isCurrentPopularScreen)
                            stringResource(R.string.popular_movies)
                        else
                            stringResource(R.string.upcoming_movies),
                        fontSize = 20.sp,
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize().
                padding(it)
        ){
            BottomNavHost(
                bottomNavController = bottomNavController,
                navController = navController,
                route = Screen.PopularMovies.route,
                model = movieListViewModel
            )
        }
    }
}