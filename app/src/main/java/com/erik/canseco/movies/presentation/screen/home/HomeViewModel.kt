package com.erik.canseco.movies.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.domain.util.Resource
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
            movieListRepository.getMovieList(state.typeShared).collectLatest {
                when(it){
                    is Resource.Error -> {
                        state = state.copy(
                            error = it.message
                        )
                    }
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = it.isLoading
                        )
                    }
                    is Resource.Success -> {
                        state = state.copy(
                            movies = it.data ?: emptyList()
                        )
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