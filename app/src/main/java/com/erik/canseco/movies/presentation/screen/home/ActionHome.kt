package com.erik.canseco.movies.presentation.screen.home

import com.erik.canseco.movies.domain.model.Movie

sealed interface ActionHome {
    data object MoviesPopular: ActionHome
    data object MoviesTopRated: ActionHome
    data object MoviesNowPlaying: ActionHome
    data class  DetailMovie(val movie: String): ActionHome

}