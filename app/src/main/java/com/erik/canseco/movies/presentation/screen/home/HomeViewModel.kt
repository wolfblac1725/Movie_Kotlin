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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            movieListRepository.updateGenres()
        }
    }
    var state by mutableStateOf(HomeState())
        private set

    private val moviesByCategory: Map<TypeShared, Flow<PagingData<Movie>>> =
        listOf(
            TypeShared.Popular,
            TypeShared.TopRated,
            TypeShared.NowPlaying
        ).associateWith { category ->
            movieListRepository.getMovieListPaging2(category).cachedIn(viewModelScope)
        }

    fun moviesFor(category: TypeShared): Flow<PagingData<Movie>> =
        moviesByCategory.getValue(category)

    fun onAction(action: ActionHome) {
        when (action) {
            is ActionHome.MoviesPopular -> {
                state = state.copy(selectedCategory = TypeShared.Popular)
            }

            is ActionHome.MoviesTopRated -> {
                state = state.copy(selectedCategory = TypeShared.TopRated)
            }

            is ActionHome.MoviesNowPlaying -> {
                state = state.copy(selectedCategory = TypeShared.NowPlaying)
            }
            else -> {

            }
        }
    }
}
