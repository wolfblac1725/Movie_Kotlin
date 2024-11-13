package com.erik.canseco.movies.movielist.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.erik.canseco.movies.movielist.data.local.movie.MovieEntity
import com.erik.canseco.movies.movielist.data.remote.response.MovieCastDto
import com.erik.canseco.movies.movielist.domain.model.Cast
import com.erik.canseco.movies.movielist.domain.model.Movie
import com.erik.canseco.movies.movielist.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun  getMoviesList (
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>

    fun getMovieList2(category: String): Flow<PagingData<Movie>>

    fun getMovieListCategoryPagination(category: String): Flow<PagingData<Movie>>

    fun getMovieListPagination(category: String): Pager<Int, MovieEntity>

    fun getMovieCast(idMovie: Int): Flow<Resource<List<Cast>>>
}