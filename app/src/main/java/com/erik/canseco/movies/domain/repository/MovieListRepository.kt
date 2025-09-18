package com.erik.canseco.movies.domain.repository

import androidx.paging.PagingData
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.TypeShared
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    fun getMovieListPaging(type: TypeShared): Flow<PagingData<Movie>>
    fun getMovieListPaging2(type: TypeShared): Flow<PagingData<Movie>>
    fun getMovieCastDetail(idMovie: Int): Flow<Resource<List<Cast>>>
}