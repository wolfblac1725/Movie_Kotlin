package com.erik.canseco.movies.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.erik.canseco.movies.core.component.MovieItem
import com.erik.canseco.movies.movielist.domain.util.Category
import com.erik.canseco.movies.movielist.presentation.MovieListState
import com.erik.canseco.movies.movielist.presentation.MovieListUIEvent

@Composable
fun UpcomingMoviesScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUIEvent) -> Unit
) {
    if (movieListState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            Alignment.Center
        ){
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListState.moviesUpcoming.size) { index ->
                MovieItem(
                    movie = movieListState.moviesUpcoming[index],
                    navHostController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (index >= movieListState.moviesUpcoming.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUIEvent.Paginate(category = Category.UPCOMING))
                }
            }
        }

    }

}
