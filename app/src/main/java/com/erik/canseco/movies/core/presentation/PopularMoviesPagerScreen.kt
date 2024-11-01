package com.erik.canseco.movies.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.erik.canseco.movies.core.component.MovieItem
import com.erik.canseco.movies.movielist.presentation.MovieListViewModel

@Composable
fun PopularMoviesPagerScreen(
    model: MovieListViewModel,
    navController: NavHostController,
) {

    val movieLazyPagingItems = model.movieMediatorPopular.collectAsLazyPagingItems()

    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp) ,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        items(movieLazyPagingItems.itemCount) { index ->
            movieLazyPagingItems[index]?.let {
                MovieItem(
                    movie = it,
                    navHostController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
