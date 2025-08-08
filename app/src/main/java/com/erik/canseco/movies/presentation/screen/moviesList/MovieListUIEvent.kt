package com.erik.canseco.movies.presentation.screen.moviesList

sealed interface MovieListUIEvent {
    data class Navigate(val category: String) : MovieListUIEvent {

    }


}