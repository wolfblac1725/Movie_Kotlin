package com.erik.canseco.movies.movielist.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.erik.canseco.movies.movielist.data.local.movie.MovieDatabase
import com.erik.canseco.movies.movielist.data.local.movie.MovieEntity
import com.erik.canseco.movies.movielist.data.mappers.toCast
import com.erik.canseco.movies.movielist.data.mappers.toCastEntity
import com.erik.canseco.movies.movielist.data.mappers.toMovie
import com.erik.canseco.movies.movielist.data.mappers.toMovieEntity
import com.erik.canseco.movies.movielist.data.paging.MovieRemoteMediator
import com.erik.canseco.movies.movielist.data.paging.MoviesPagingSource
import com.erik.canseco.movies.movielist.data.remote.MovieApi
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.domain.util.Resource
import com.erik.canseco.movies.utility.TypeShared
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
): MovieListRepository {
    companion object {
        const val MAX_ITEMS_PER_PAGE = 20
        const val PRE_FETCH_DISTANCE = 3
    }

    override suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            //val localMoviesList = movieDatabase.movieDao.getMoviesListByCategory(category)
            //val shouldLoadLocalMovie = localMoviesList.isNotEmpty() && !forceFetchFromRemote
            /*if(shouldLoadLocalMovie) {
                emit(Resource.Success(data = localMoviesList.map { it.toMovie(category) }))
                emit(Resource.Loading(false))
                return@flow
            }*/
            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            } catch (e: Exception){
                    e.printStackTrace()
                    emit(Resource.Error(message = "Error Loading Movies"))
                    return@flow
            }
            val movieEntities = movieListFromApi.results.let { it.map { movieDto -> movieDto.toMovieEntity(category)} }
                .toMutableList()
            for ((index,movieEntity) in movieEntities.withIndex()) {
                movieDatabase.movieDao.getMovieById(movieEntity.id).let { movie ->
                    movie?.category?.let {
                        if (!it.contains(category)) {
                            Log.e(
                                "MovieListRepositoryImpl",
                                "getMoviesList final: ${movie.category + "," + category}"
                            )
                            movieEntities[index] = movieEntities[index].copy(category = movie.category + "," + category)
                            Log.e(
                                "MovieListRepositoryImpl",
                                "getMoviesList movieEntities: ${movieEntities[index].category}"
                            )
                        }
                    }
                }
            }
            movieDatabase.movieDao.updateMovieList(movieEntities)
            emit(Resource.Success(movieEntities.map { it.toMovie(category)}))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDao.getMovieById(id)
            if (movieEntity != null) {
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such movie"))
            emit(Resource.Loading(false))


        }
    }

    override fun getMovieList2(category: String): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                prefetchDistance = 10
            )
        ){
            movieDatabase.movieDao.getMoviesListCategoryPagination(category)

        }.flow.map { value: PagingData<MovieEntity> ->
            value.map { it.toMovie(it.category) }

        }
    }
    override fun getMovieListCategoryPagination(category: String): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = MAX_ITEMS_PER_PAGE,
                prefetchDistance = PRE_FETCH_DISTANCE
            ),
            pagingSourceFactory = { MoviesPagingSource(movieApi,category) }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovieListPagination(category: String): Pager<Int, MovieEntity> {
        return Pager(
            PagingConfig(
                pageSize = MAX_ITEMS_PER_PAGE,
                prefetchDistance = PRE_FETCH_DISTANCE
            ),
            remoteMediator = MovieRemoteMediator(category,movieDatabase,movieApi),
            pagingSourceFactory = { movieDatabase.movieDao.getMoviesListCategoryPagination(category)
            }
        )
    }

    override fun getMovieCast(idMovie: Int): Flow<Resource<List<Cast>>> {
        return flow {
            emit(Resource.Loading(true))
            val movieCast = movieDatabase.movieDao.getCastByMovieId(idMovie)

            if (movieCast.isNotEmpty()) {
                emit(Resource.Success(movieCast.map { it.toCast(idMovie) }))
                emit(Resource.Loading(false))
                return@flow
            } else {
                val movieCastApi = movieApi.getCredits(idMovie)
                movieDatabase.movieDao.updateCast(movieCastApi.cast.map { it.toCastEntity(idMovie) })
                emit(Resource.Success(movieCastApi.cast.map { it.toCastEntity(idMovie)}.map { it.toCast(idMovie)}))
                emit(Resource.Loading(false))
            }

        }
    }

    override fun getMovieList(type: TypeShared): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            when (type){
                is TypeShared.Popular -> {
                    emit(Resource.Success(movieApi.getMoviesList("popular",1).results.map{it.toMovie("popular")}))
                    emit(Resource.Loading(false))
                    return@flow
                }
                is TypeShared.TopRated -> {
                    emit(Resource.Success(movieApi.getMoviesList("top_rated",1).results.map { it.toMovie("top_rated")}))
                    emit(Resource.Loading(false))
                    return@flow
                }
                is TypeShared.NowPlaying -> {
                    emit(Resource.Success(movieApi.getMoviesList("now_playing",1).results.map { it.toMovie("now_playing") }))
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