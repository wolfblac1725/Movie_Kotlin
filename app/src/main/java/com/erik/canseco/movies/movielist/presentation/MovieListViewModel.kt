package com.erik.canseco.movies.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.erik.canseco.movies.movielist.data.mappers.toMovie
import com.erik.canseco.movies.movielist.domain.repository.MovieListRepository
import com.erik.canseco.movies.movielist.domain.util.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    movieListRepository: MovieListRepository
): ViewModel() {
    private var _moviesListState = MutableStateFlow(MovieListState())
    val moviesListState = _moviesListState.asStateFlow()
    // val moviesList = movieListRepository.getMovieList2(Category.POPULAR).cachedIn(viewModelScope)
    // val movieListRed = movieListRepository.getMovieListCategoryPagination(Category.POPULAR).cachedIn(viewModelScope)
    val movieMediatorPopular = movieListRepository.getMovieListPagination(Category.POPULAR)
        .flow.map { pagingData -> pagingData.map { it.toMovie(Category.POPULAR) }}.cachedIn(viewModelScope)

    val movieMediatorUpcoming = movieListRepository.getMovieListPagination(Category.UPCOMING)
        .flow.map { pagingData -> pagingData.map { it.toMovie(Category.UPCOMING) }}.cachedIn(viewModelScope)

    fun onEvent(event: MovieListUIEvent) {
        when(event) {
            is MovieListUIEvent.Navigate -> {
                if (event.category == Category.POPULAR) {
                    _moviesListState.update { it.copy( isCurrentPopularScreen = true ) }
                } else if(event.category == Category.UPCOMING) {
                    _moviesListState.update { it.copy( isCurrentPopularScreen = false ) }
                }
            }
        }
    }
}