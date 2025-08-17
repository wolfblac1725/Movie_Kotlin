package com.erik.canseco.movies.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.TypeShared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   val  movieListRepository: MovieListRepository
):ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        setCategory(TypeShared.TopRated)
        getMovies()
    }

    fun getMovies () {
        viewModelScope.launch {
            movieListRepository.getMovieList(state.typeShared, page = 1).collectLatest {
                when(it){
                    is Resource.Error -> {
                        state = when (state.typeShared) {
                            TypeShared.Popular ->{
                                state.copy(moviesPopular = state.moviesPopular.copy( error = it.message))
                            }

                            TypeShared.TopRated ->{
                                state.copy(moviesTopRated = state.moviesTopRated.copy( error = it.message))
                            }

                            TypeShared.NowPlaying ->{
                                state.copy(moviesNowPlaying = state.moviesNowPlaying.copy( error = it.message))
                            }
                        }
                    }
                    is Resource.Loading -> {
                        state = when (state.typeShared) {
                            TypeShared.Popular -> {
                                state.copy(moviesPopular = state.moviesPopular.copy(isLoading = it.isLoading))
                            }

                            TypeShared.TopRated -> {
                                state.copy(moviesTopRated = state.moviesTopRated.copy(isLoading = it.isLoading))
                            }

                            TypeShared.NowPlaying -> {
                                state.copy(moviesNowPlaying = state.moviesNowPlaying.copy(isLoading = it.isLoading))
                            }
                        }
                    }
                    is Resource.Success -> {
                        state = when (state.typeShared) {
                            TypeShared.Popular -> {
                                state.copy(moviesPopular = state.moviesPopular.copy(movies = it.data ?: emptyList()))
                            }

                            TypeShared.TopRated -> {
                                state.copy(moviesTopRated = state.moviesTopRated.copy(movies = it.data ?: emptyList()))
                            }

                            TypeShared.NowPlaying -> {
                                state.copy(moviesNowPlaying = state.moviesNowPlaying.copy(movies = it.data ?: emptyList()))
                            }
                        }
                    }
                }
            }
        }
    }
    fun onAction(action: ActionHome) {
        when (action) {
            is ActionHome.MoviesPopular -> {
                state = state.copy(typeShared = TypeShared.Popular)
                getMovies()
            }

            is ActionHome.MoviesTopRated -> {
                state = state.copy(typeShared = TypeShared.TopRated)
                getMovies()
            }

            is ActionHome.MoviesNowPlaying -> {
                state = state.copy(typeShared = TypeShared.NowPlaying)
                getMovies()
            }
            else -> {

            }
        }
    }
    fun setCategory(type: TypeShared) {
        state = state.copy(typeShared = type)
    }
}