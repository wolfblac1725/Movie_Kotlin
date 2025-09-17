package com.erik.canseco.movies.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.utility.TypeShared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val _currentCategory = MutableStateFlow(state.typeShared)

    val movies: Flow<PagingData<Movie>> = _currentCategory.flatMapLatest { category ->
        movieListRepository.getMovieListPaging2(category)
    }.cachedIn(viewModelScope)


    fun onAction(action: ActionHome) {
        when (action) {
            is ActionHome.MoviesPopular -> {
                _currentCategory.value = TypeShared.Popular
                state = state.copy(typeShared = TypeShared.Popular)
            }

            is ActionHome.MoviesTopRated -> {
                _currentCategory.value = TypeShared.TopRated
                state = state.copy(typeShared = TypeShared.TopRated)
            }

            is ActionHome.MoviesNowPlaying -> {
                _currentCategory.value = TypeShared.NowPlaying
                state = state.copy(typeShared = TypeShared.NowPlaying)
            }
            else -> {

            }
        }
    }
}
