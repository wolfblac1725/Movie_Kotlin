package com.erik.canseco.movies.movielist.data.repository

import android.util.Log
import com.erik.canseco.movies.movielist.data.local.movie.MovieDatabase
import com.erik.canseco.movies.movielist.data.mappers.toMovie
import com.erik.canseco.movies.movielist.data.mappers.toMovieEntity
import com.erik.canseco.movies.movielist.data.remote.MovieApi
import com.erik.canseco.movies.movielist.domain.model.Movie
import com.erik.canseco.movies.movielist.domain.repository.MovieListRepository
import com.erik.canseco.movies.movielist.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
): MovieListRepository {
    override suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMoviesList = movieDatabase.movieDao.getMoviesListByCategory(category)
            val shouldLoadLocalMovie = localMoviesList.isNotEmpty() && !forceFetchFromRemote
            if(shouldLoadLocalMovie) {
                emit(Resource.Success(data = localMoviesList.map { it.toMovie(category) }))
                emit(Resource.Loading(false))
                return@flow
            }
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
                    if ( movie != null && !movie.category.contains(category)) {
                        Log.e("MovieListRepositoryImpl", "getMoviesList final: ${movie.category + "," + category}")
                        movieEntities.set(index,movieEntities[index].copy(category = movie.category + "," + category))
                        Log.e("MovieListRepositoryImpl", "getMoviesList movieEntities: ${movieEntities[index].category}")
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
}