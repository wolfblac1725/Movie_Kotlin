package com.erik.canseco.movies.movielist.presentation

sealed interface MovieListUIEvent {
    data class Paginate(val category: String) : MovieListUIEvent
    data class Navigate(val category: String) : MovieListUIEvent {

    }


}