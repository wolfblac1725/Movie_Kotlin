package com.erik.canseco.movies.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.erik.canseco.movies.R
import com.erik.canseco.movies.presentation.screen.home.ActionHome
import com.erik.canseco.movies.utility.TypeShared
import com.erik.canseco.movies.utility.BottomItem

@Composable
fun BottomNavigationBar(
    selectedCategory: TypeShared,
    onEvent: (ActionHome) -> Unit
) {
    val items = listOf(
        BottomItem(stringResource(R.string.top_rated), Icons.Rounded.Movie) to
            TypeShared.TopRated,
        BottomItem(stringResource(R.string.popular), Icons.Rounded.Upcoming) to
            TypeShared.Popular,
        BottomItem(stringResource(R.string.now_playing), Icons.Rounded.PlayCircleFilled) to
            TypeShared.NowPlaying
    )
    NavigationBar {
        items.forEach { (item, category) ->
            val action = when (category) {
                TypeShared.TopRated -> ActionHome.MoviesTopRated
                TypeShared.Popular -> ActionHome.MoviesPopular
                TypeShared.NowPlaying -> ActionHome.MoviesNowPlaying
            }
            NavigationBarItem(
                selected = selectedCategory == category,
                onClick = {
                    onEvent(action)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }

    }
}