package com.erik.canseco.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.erik.canseco.movies.data.local.MovieDatabase
import com.erik.canseco.movies.data.local.entity.MovieEntity
import com.erik.canseco.movies.data.local.entity.MoviePage
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.utility.TypeShared
import com.erik.canseco.movies.utility.UtilityFun
import com.erik.canseco.movies.utility.mappers.toMovieEntity
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase,
    type: TypeShared,
): RemoteMediator<Int, MovieEntity>(){
    private val movieDao = movieDatabase.movieDao
    val category = UtilityFun.toCategory(type)
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - movieDao.lastUpdated(category) <= cacheTimeout)
        {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity >
    ): MediatorResult {
        return try {
            val loadKey= when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val remoteKey = movieDatabase.withTransaction {
                        movieDao.getMoviePageByCategory(category)
                    }
                    remoteKey?.let {
                        it.page + 1
                    }?: 1
                }
            }
            val response = movieApi.getMoviesList(category, loadKey)
            movieDatabase.withTransaction {
                if(loadType == LoadType.REFRESH){
                    movieDao.clearMoviesForCategory(category)
                    movieDao.clearMoviePageByCategory(category)
                }

                val movieEntity = response.results.map { it.toMovieEntity(category)}
                movieDao.updateMovieListCategory(movieEntity)
                movieDao.updateMoviePage(MoviePage(category, response.page, response.total_pages))
            }
            MediatorResult.Success(response.results.isEmpty())
        }catch (e: IOException){
            MediatorResult.Error(e)
        }catch (e: Exception){
            MediatorResult.Error(e)
        }
    }
}
