package com.erik.canseco.movies.movielist.data.paging

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.erik.canseco.movies.movielist.data.local.movie.MovieDatabase
import com.erik.canseco.movies.movielist.data.local.movie.MovieEntity
import com.erik.canseco.movies.movielist.data.local.movie.MovieKey
import com.erik.canseco.movies.movielist.data.mappers.toMovieEntity
import com.erik.canseco.movies.movielist.data.remote.MovieApi
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val category: String,
    private val database: MovieDatabase,
    private val movieApi: MovieApi
): RemoteMediator<Int, MovieEntity>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey =  when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null){
                        1
                    } else {
                       database.movieDao.getMovieKey(category).first().next ?: 1
                    }
                }
            }
            val movies =  movieApi.getMoviesList(category, loadKey)

            database.withTransaction {
                if(loadType == LoadType.REFRESH){
                    database.movieDao.clearMovieList()
                }
                val movieEntities = movies.results.let { it.map { movieDto -> movieDto.toMovieEntity(category)} }
                    .toMutableList()
                for ((index,movieEntity) in movieEntities.withIndex()) {
                    database.movieDao.getMovieById(movieEntity.id).let { movie ->
                        if (!movie.category.contains(category)) {
                            Log.e("MovieListRepositoryImpl", "getMoviesList final: ${movie.category + "," + category}")
                            movieEntities.set(index,movieEntities[index].copy(category = movie.category + "," + category))
                            Log.e("MovieListRepositoryImpl", "getMoviesList movieEntities: ${movieEntities[index].category}")
                        }
                    }
                }
                database.movieDao.updateMovieList(movieEntities)
                database.movieDao.updateMovieKey(MovieKey(id = category ,movies.page,movies.page + 1))
            }
            MediatorResult.Success(
                endOfPaginationReached = movies.page == movies.total_pages
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException){
            MediatorResult.Error(e)
        }
    }
}