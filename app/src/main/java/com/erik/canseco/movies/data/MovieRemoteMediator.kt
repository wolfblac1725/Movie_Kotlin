package com.erik.canseco.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.erik.canseco.movies.data.local.MovieDatabase
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.data.remote.response.MovieListDto
import com.erik.canseco.movies.utility.TypeShared
import com.erik.canseco.movies.utility.UtilityFun
import com.erik.canseco.movies.utility.mappers.toMovieEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase,
    private val type: TypeShared,
): RemoteMediator<Int, MovieListDto>(){
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int,MovieListDto >
    ): MediatorResult {
        return try {
            val loadKey=when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val item = state.lastItemOrNull()
                    if(item == null){
                        1
                    } else{
                        item.page + 1
                    }
                }
            }
            val category= UtilityFun.toCategory(type)
            val response = movieApi.getMoviesList(category, loadKey)
            movieDatabase.withTransaction {
                if(loadType == LoadType.REFRESH){
                    movieDatabase.movieDao.clearMoviesForCategory(category)
                }

                val movieEntity = response.results.map { it.toMovieEntity(category)}
                movieDatabase.movieDao.updateMovieListCategory(movieEntity)
            }
            MediatorResult.Success(response.results.isEmpty())
        }catch (e: IOException){
            MediatorResult.Error(e)
        }catch (e: Exception){
            MediatorResult.Error(e)
        }
    }
}
