package com.erik.canseco.movies.domain.repository

import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.TypeShared
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    fun getMovieList(type: TypeShared): Flow<Resource<List<Movie>>>
    fun getMovieCastDetail(idMovie: Int): Flow<Resource<List<Cast>>>
}