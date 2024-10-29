package com.erik.canseco.movies.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erik.canseco.movies.movielist.domain.repository.MovieListRepository
import com.erik.canseco.movies.movielist.domain.util.Category
import com.erik.canseco.movies.movielist.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {
    private var _moviesListState = MutableStateFlow(MovieListState())
    val moviesListState = _moviesListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }
    fun onEvent(event: MovieListUIEvent) {
        when(event) {
            is MovieListUIEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if(event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }
            is MovieListUIEvent.Navigate -> {
                if (event.category == Category.POPULAR) {
                    _moviesListState.update { it.copy( isCurrentPopularScreen = true ) }
                } else if(event.category == Category.UPCOMING) {
                    _moviesListState.update { it.copy( isCurrentPopularScreen = false ) }
                }
            }
        }
    }
    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _moviesListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMoviesList(
                forceFetchFromRemote = forceFetchFromRemote,
                category = Category.UPCOMING,
                page = moviesListState.value.upcomingPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading ->{
                        _moviesListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Error -> {
                        _moviesListState.update {
                            it.copy(isLoading = false)
                        }

                    }
                    is Resource.Success -> {
                        result.data?.let { upComingMoviesList ->
                            _moviesListState.update {
                                it.copy(
                                    moviesUpcoming = moviesListState.value.moviesUpcoming + upComingMoviesList,
                                    upcomingPage = moviesListState.value.upcomingPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _moviesListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMoviesList(
                forceFetchFromRemote = forceFetchFromRemote,
                category = Category.POPULAR,
                page = moviesListState.value.popularPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading ->{
                        _moviesListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Error -> {
                        _moviesListState.update {
                            it.copy(isLoading = false)
                        }

                    }
                    is Resource.Success -> {
                        result.data?.let { popularMoviesList ->
                            _moviesListState.update {
                                it.copy(
                                    moviesPopular = moviesListState.value.moviesPopular + popularMoviesList,
                                    popularPage = moviesListState.value.popularPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}