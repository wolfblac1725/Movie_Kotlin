package com.erik.canseco.movies.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.hilt.navigation.compose.hiltViewModel
import com.erik.canseco.movies.presentation.component.BottomNavigationBar
import com.erik.canseco.movies.presentation.component.ListViewComponent
import com.erik.canseco.movies.presentation.component.Loader
import com.erik.canseco.movies.utility.TypeShared


@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    HomeScreen(
        state = homeViewModel.state,
        modifier = modifier,
        action = { action ->
            when(action) {
                is ActionHome.DetailMovie -> {
                    onNavigate(action.movie)
                }
                else ->{
                    homeViewModel.onAction(action)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    action: (ActionHome) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Movie")
                }
            )
        },
        content = { padding ->

            when(state.typeShared){
                TypeShared.Popular -> {
                    if(state.moviesPopular.isLoading)
                        Loader()
                    else if (state.moviesPopular.error != null)
                        Text(text = state.moviesPopular.error)
                    else
                        ListViewComponent(
                            column = 2,
                            movies = state.moviesPopular.movies,
                            action = action,
                            modifier = Modifier.padding(padding)
                        )
                }
                TypeShared.TopRated -> {
                    if(state.moviesTopRated.isLoading)
                        Loader()
                    else if (state.moviesTopRated.error != null)
                        Text(text = state.moviesTopRated.error)
                    else
                        ListViewComponent(
                            column = 2,
                            movies = state.moviesTopRated.movies,
                            action = action,
                            modifier = Modifier.padding(padding)
                        )
                }
                TypeShared.NowPlaying -> {
                    if(state.moviesNowPlaying.isLoading)
                        Loader()
                    else if (state.moviesNowPlaying.error != null)
                        Text(text = state.moviesNowPlaying.error)
                    else
                        ListViewComponent(
                            column = 2,
                            movies = state.moviesNowPlaying.movies,
                            action = action,
                            modifier = Modifier.padding(padding)
                        )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                onEvent = action
            )
        }
    )
}