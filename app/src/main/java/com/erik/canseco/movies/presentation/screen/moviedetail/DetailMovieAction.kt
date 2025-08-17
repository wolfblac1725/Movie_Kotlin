package com.erik.canseco.movies.presentation.screen.moviedetail

sealed interface DetailMovieAction {
    data object Back: DetailMovieAction
}