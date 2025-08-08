package com.erik.canseco.movies.presentation.screen.moviedetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    val  movieListRepository: MovieListRepository
) : ViewModel() {
    var state by mutableStateOf(DetailState())

    fun setMovie(movie: Movie) {
        state = state.copy(movie = movie)
    }
    fun getCast(idMovie: Int) {
        viewModelScope.launch {
            movieListRepository.getMovieCastDetail(idMovie).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        state = state.copy(
                            error = result.message
                        )
                    }
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = result.isLoading
                        )
                    }
                    is Resource.Success -> {
                        state = state.copy(
                            cast = result.data ?: emptyList()
                        )
                    }
                }

            }
        }
    }

}