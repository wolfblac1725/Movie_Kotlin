package com.erik.canseco.movies.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.erik.canseco.movies.movielist.data.local.movie.MovieEntity
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.util.Resource
import com.erik.canseco.movies.utility.TypeShared
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

    fun getMovieList(type: TypeShared): Flow<Resource<List<Movie>>>
    fun getMovieCastDetail(idMovie: Int): Flow<Resource<List<Cast>>>
}