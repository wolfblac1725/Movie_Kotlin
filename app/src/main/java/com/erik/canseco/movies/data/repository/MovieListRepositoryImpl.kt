package com.erik.canseco.movies.data.repository

import com.erik.canseco.movies.movielist.data.local.movie.MovieDatabase
import com.erik.canseco.movies.movielist.data.mappers.toCast
import com.erik.canseco.movies.movielist.data.mappers.toMovie
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

    override fun getMovieList(type: TypeShared): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            when (type){
                is TypeShared.Popular -> {
                    emit(Resource.Success(movieApi.getMoviesList(Category.POPULAR,1).results.map{it.toMovie(Category.POPULAR)}))
                    emit(Resource.Loading(false))
                    return@flow
                }
                is TypeShared.TopRated -> {
                    emit(Resource.Success(movieApi.getMoviesList(Category.TOP_RATED,1).results.map { it.toMovie(
                        Category.TOP_RATED)}))
                    emit(Resource.Loading(false))
                    return@flow
                }
                is TypeShared.NowPlaying -> {
                    emit(Resource.Success(movieApi.getMoviesList(Category.NOW_PLAYING,1).results.map { it.toMovie(Category.NOW_PLAYING) }))
                    emit(Resource.Loading(false))
                    return@flow
                }
            }
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