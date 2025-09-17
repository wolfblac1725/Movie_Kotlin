package com.erik.canseco.movies.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import coil3.network.HttpException
import com.erik.canseco.movies.data.MoviePagingSource
import com.erik.canseco.movies.data.MovieRemoteMediator
import com.erik.canseco.movies.data.local.MovieDatabase
import com.erik.canseco.movies.utility.mappers.toCast
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.TypeShared
import com.erik.canseco.movies.utility.UtilityFun
import com.erik.canseco.movies.utility.mappers.toCastEntity
import com.erik.canseco.movies.utility.mappers.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase,
): MovieListRepository {
    private val movieDao = movieDatabase.movieDao


    @OptIn(ExperimentalPagingApi::class)
    override fun getMovieListPaging(type: TypeShared): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )){
                MoviePagingSource(movieApi,type)
            }.flow
    }
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovieListPaging2(type: TypeShared): Flow<PagingData<Movie>> {
        val category = UtilityFun.toCategory(type)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(
                movieApi,
                movieDatabase,
                type
            )
        ){
            if(type == TypeShared.Popular){
                movieDao.getMoviesListCategoryPaginationPopular(category)
            }else if(type == TypeShared.TopRated) {
                movieDao.getMoviesListCategoryPaginationToRated(category)
            } else {
                movieDao.getMoviesListCategoryPaginationNowPlaying(category)
            }
        }.flow.map { pagingData ->
            pagingData.map {
                it.toMovie(category)
            }

        }
    }

    override fun getMovieCastDetail(idMovie: Int): Flow<Resource<List<Cast>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val movieCast = movieApi.getCredits(idMovie).cast.map { it.toCastEntity(idMovie) }
                movieDao.updateCast(movieCast)
            } catch (e:HttpException){
                Log.e("Error",e.toString())

            }catch (e: IOException){
                Log.e("Error",e.toString())
            }
            val localMovieCast = movieDao.getCastByMovieId(idMovie)
            if(localMovieCast.isNotEmpty()){
                emit(Resource.Success(localMovieCast.map { it.toCast(idMovie) }))
            }else{
                emit(Resource.Error("No se pudieron cargar los actores"))
            }
            emit(Resource.Loading(false))
            return@flow
        }
    }
}