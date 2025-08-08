package com.erik.canseco.movies.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.hilt.navigation.compose.hiltViewModel
import com.erik.canseco.movies.presentation.component.BottomNavigationBar
import com.erik.canseco.movies.presentation.component.Loader
import com.erik.canseco.movies.presentation.component.MovieItem
import kotlinx.serialization.json.Json


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
            if(state.isLoading) {
                Loader()
            }else if(state.error != null) {
                Text(text = state.error)
            } else{
                LazyVerticalGrid(
                    modifier = Modifier.padding(padding),
                    columns = GridCells.Fixed(2)
                ) {
                    items(state.movies.size) {
                        MovieItem(
                            movie = state.movies[it],
                            onClick = {
                                val json = Json { ignoreUnknownKeys = true }
                                val movie = state.movies[it]
                                val movieJson = json.encodeToString(movie)
                                action(ActionHome.DetailMovie(movieJson))
                            }
                        )
                    }
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