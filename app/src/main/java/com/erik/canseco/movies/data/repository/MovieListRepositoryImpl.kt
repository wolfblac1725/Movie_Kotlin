package com.erik.canseco.movies.data.repository

import com.erik.canseco.movies.data.local.MovieDatabase
import com.erik.canseco.movies.utility.mappers.toCast
import com.erik.canseco.movies.utility.mappers.toMovie
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.Category
import com.erik.canseco.movies.utility.TypeShared
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
): MovieListRepository {
    override fun getMovieList(type: TypeShared, page: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val movieApi = when (type){
                is TypeShared.Popular -> {
                    movieApi.getMoviesList(Category.POPULAR, page)
                }
                is TypeShared.TopRated -> {
                    movieApi.getMoviesList(Category.TOP_RATED, page)
                }
                is TypeShared.NowPlaying -> {
                    movieApi.getMoviesList(Category.NOW_PLAYING, page)
                }
            }
            val response = movieApi.results.map { it.toMovie(type.toString())}
            emit(Resource.Success(response))
            emit(Resource.Loading(false))
            return@flow
        }
    }


    override fun getMovieCastDetail(idMovie: Int): Flow<Resource<List<Cast>>> {
        return flow {
            emit(Resource.Loading(true))
            val movieCast = movieApi.getCredits(idMovie).cast.map { it.toCast() }
            emit(Resource.Success(movieCast))
            emit(Resource.Loading(false))
            return@flow
        }
    }
}