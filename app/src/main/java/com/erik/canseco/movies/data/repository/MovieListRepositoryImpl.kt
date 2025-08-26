package com.erik.canseco.movies.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import coil3.network.HttpException
import com.erik.canseco.movies.data.MoviePagingSource
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
import com.erik.canseco.movies.utility.mappers.toCastEntity
import com.erik.canseco.movies.utility.mappers.toMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    movieDatabase: MovieDatabase,
): MovieListRepository {
    private val movieDao = movieDatabase.movieDao

    override fun getMovieList(type: TypeShared, page: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val movieApi = when (type) {
                    is TypeShared.Popular -> movieApi.getMoviesList(Category.POPULAR, page)
                    is TypeShared.TopRated -> movieApi.getMoviesList(Category.TOP_RATED, page)
                    is TypeShared.NowPlaying -> movieApi.getMoviesList(Category.NOW_PLAYING, page)
                }
                val listMovie = movieApi.results.map { it.toMovieEntity(type.toString()) }
                movieDao.updateMovieList(listMovie)

            }catch (e: HttpException){
                Log.e("Error",e.toString())

            }catch (e: IOException){
                Log.e("Error",e.toString())
            }
            val localMovies = movieDao.getMoviesListByCategory(type.toString())
            if(localMovies.isNotEmpty()){
                emit(Resource.Success(localMovies.map { it.toMovie(type.toString()) }))
            } else{
                emit(Resource.Error("No se pudieron cargar las peliculas"))
            }
            emit(Resource.Loading(false))
            return@flow
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovieListPaging(type: TypeShared): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApi,type)
            }
        ).flow
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