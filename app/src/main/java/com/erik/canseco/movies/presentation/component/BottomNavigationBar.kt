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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.erik.canseco.movies.R
import com.erik.canseco.movies.presentation.screen.home.ActionHome
import com.erik.canseco.movies.utility.BottomItem

@Composable
fun BottomNavigationBar(
    onEvent: (ActionHome) -> Unit
) {
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.top_rated),
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Upcoming
        ),
        BottomItem(
            title = stringResource(R.string.now_playing),
            icon = Icons.Rounded.PlayCircleFilled
        )
    )
    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem.intValue == index,
                onClick = {
                    selectedItem.intValue = index
                    when (selectedItem.intValue){
                        0 -> {
                            onEvent(ActionHome.MoviesTopRated)
                        }
                        1 -> {
                            onEvent(ActionHome.MoviesNowPlaying)
                        }
                        2 -> {
                            onEvent(ActionHome.MoviesPopular)
                        }
                    }
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