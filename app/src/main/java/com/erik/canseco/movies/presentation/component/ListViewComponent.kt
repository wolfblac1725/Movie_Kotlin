package com.erik.canseco.movies.presentation.component

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.presentation.screen.home.ActionHome
import kotlinx.serialization.json.Json

@Composable
fun ListViewComponent(
    column: Int = 2,
    movies: List<Movie>,
    action: (ActionHome) -> Unit,
    modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(column)
    ) {
        items(movies.size) {
            MovieItem(
                movie = movies[it],
                onClick = {
                    val json = Json { ignoreUnknownKeys = true }
                    val movie = movies[it]
                    val movieJson = json.encodeToString(movie)
                    action(ActionHome.DetailMovie(movieJson))
                }
            )
        }
    }

}