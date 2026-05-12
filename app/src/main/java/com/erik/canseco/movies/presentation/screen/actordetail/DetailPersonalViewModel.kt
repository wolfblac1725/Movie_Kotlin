package com.erik.canseco.movies.presentation.screen.actordetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.registerForAllProfilingResults
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailPersonalViewModel @Inject constructor(
    val  movieListRepository: MovieListRepository
) : ViewModel() {
    var state by mutableStateOf(DetailPersonalState())

    fun getPersonal(idPerson: Int){
        viewModelScope.launch {
            movieListRepository.getPersonal(idPerson).collectLatest { result ->
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
                            actor = result.data
                        )
                    }
                }
            }
        }
    }
    fun getPersonalMovies(idPerson: Int){
        viewModelScope.launch {
            movieListRepository.getPersonalMovies(idPerson).collectLatest { result ->
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
                            movies = result.data
                        )
                    }

                }
            }
        }
    }
}