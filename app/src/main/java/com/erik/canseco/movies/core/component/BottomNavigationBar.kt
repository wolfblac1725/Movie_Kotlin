package com.erik.canseco.movies.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.erik.canseco.movies.R
import com.erik.canseco.movies.core.util.BottomItem
import com.erik.canseco.movies.movielist.domain.util.Category
import com.erik.canseco.movies.movielist.domain.util.Screen
import com.erik.canseco.movies.movielist.presentation.MovieListUIEvent

@Composable
fun BottomNavigationBar(
    navController: NavController,
    onEvent: (MovieListUIEvent) -> Unit
) {
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = stringResource(R.string.upcoming),
            icon = Icons.Rounded.Upcoming
        )
    )
    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItem.intValue == index,
                    onClick = {
                        selectedItem.intValue = index
                        when (selectedItem.intValue){
                            0 -> {

                                onEvent(MovieListUIEvent.Navigate(category = Category.POPULAR))
                                navController.popBackStack()
                                navController.navigate(Screen.PopularMovies.route)
                            }
                            1 -> {
                                onEvent(MovieListUIEvent.Navigate(category = Category.UPCOMING))
                                navController.popBackStack()
                                navController.navigate(Screen.UpcomingMovies.route)
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
}