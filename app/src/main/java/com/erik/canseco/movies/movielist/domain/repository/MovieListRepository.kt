package com.erik.canseco.movies.movielist.domain.repository

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
}