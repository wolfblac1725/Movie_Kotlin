package com.erik.canseco.movies.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erik.canseco.movies.movielist.domain.repository.MovieListRepository
import com.erik.canseco.movies.movielist.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private  val movieId = savedStateHandle.get<Int>("movieId")

    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()

    private val _detailCastState = MutableStateFlow(DetailCastState())
    val detailCastState = _detailCastState.asStateFlow()

    init {
        getMovie(movieId ?: -1)
        getCast (movieId ?: -1)
    }

    private fun getMovie(movieId: Int) {
        viewModelScope.launch {
            _detailState.update {
                it.copy(isLoading = false)
            }
            movieListRepository.getMovie(movieId).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _detailState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _detailState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detailState.update {
                                it.copy(movie = movie)
                            }
                        }
                    }
                }
            }
        }
    }
    private fun getCast(movieId: Int) {
        viewModelScope.launch {
            movieListRepository.getMovieCast(movieId).collectLatest { result ->
                when(result){
                    is Resource.Loading -> {
                        _detailCastState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Error -> {
                        _detailCastState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { cast ->
                            _detailCastState.update {
                                it.copy(cast = cast)
                            }
                        }
                    }
                }
            }
        }
    }
}