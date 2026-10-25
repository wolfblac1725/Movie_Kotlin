package com.erik.canseco.movies.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import retrofit2.HttpException
import com.erik.canseco.movies.data.MoviePagingSource
import com.erik.canseco.movies.data.MovieRemoteMediator
import com.erik.canseco.movies.data.local.MovieDatabase
import com.erik.canseco.movies.utility.mappers.toCast
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Genre
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.model.MovieVideo
import com.erik.canseco.movies.domain.model.Person
import com.erik.canseco.movies.domain.repository.MovieListRepository
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.TypeShared
import com.erik.canseco.movies.utility.UtilityFun
import com.erik.canseco.movies.utility.mappers.toCastEntity
import com.erik.canseco.movies.utility.mappers.toGenre
import com.erik.canseco.movies.utility.mappers.toGenreEntity
import com.erik.canseco.movies.utility.mappers.toMovie
import com.erik.canseco.movies.utility.mappers.toPerson
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
            when (type) {
                TypeShared.Popular -> {
                    movieDao.getMoviesListCategoryPaginationPopular(category)
                }
                TypeShared.TopRated -> {
                    movieDao.getMoviesListCategoryPaginationToRated(category)
                }
                else -> {
                    movieDao.getMoviesListCategoryPaginationNowPlaying(category)
                }
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
            } catch (e: HttpException) {
                Log.e("Error", e.toString())
            } catch (e: IOException) {
                Log.e("Error", e.toString())
            }
            val localMovieCast = movieDao.getCastByMovieId(idMovie)
            if (localMovieCast.isNotEmpty()) {
                emit(Resource.Success(localMovieCast.map { it.toCast(idMovie) }))
            } else {
                emit(Resource.Error("No se pudieron cargar los actores"))
            }
            emit(Resource.Loading(false))
        }
    }

    override fun getMovieTrailers(idMovie: Int): Flow<Resource<List<MovieVideo>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val trailers = movieApi.getMovieVideos(idMovie).results
                    .filter { it.site.equals("YouTube", ignoreCase = true) && it.type.equals("Trailer", ignoreCase = true) && it.key.isNotBlank() }
                    .map { video ->
                        MovieVideo(
                            key = video.key,
                            name = video.name,
                            site = video.site,
                            type = video.type
                        )
                    }
                emit(Resource.Success(trailers))
            } catch (e: IOException) {
                Log.e("Error", e.toString())
                emit(Resource.Error(e.toString()))
            } catch (e: HttpException) {
                Log.e("Error", e.toString())
                emit(Resource.Error(e.toString()))
            }
            emit(Resource.Loading(false))
        }
    }

    /*override fun getMovieDetail(idMovie: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val movieDetail = movieApi.getMovieDetail(idMovie)


            }catch (e: IOException){
                Log.e("Error",e.toString())
            }catch (e: HttpException){
                Log.e("Error",e.toString())
            }
            }
        }

    }*/
    override suspend fun updateGenres(){
            try {
                val contGenres = movieDao.getCountGenres()
                if(contGenres == 0) {
                    val movieCast = movieApi.getGenres().genres.map { it.toGenreEntity() }
                    movieDao.updateGenres(movieCast)
                }
            } catch (e: IOException) {
                Log.e("Error", e.toString())
            } catch (e: HttpException) {
                Log.e("Error", e.toString())
            }
    }
    override fun getGenres(genres: List<Int>): Flow<Resource<List<Genre>>> {
        return flow{
            emit(Resource.Loading(true))
            try {
                val movieGenres = movieDao.getGenres(genres).map { it.toGenre() }
                emit(Resource.Success(movieGenres))
            }catch (e: IOException){
                Log.e("Error",e.toString())
            }
        }

    }
    override fun getPersonal (idPerson: Int): Flow<Resource<Person>> {
        return flow{
            emit(Resource.Loading(true))

            try {
                val personResponse = movieApi.getPersonDetail(idPerson)
                val person = personResponse.toPerson()
                emit(Resource.Success(person))
            } catch (e: IOException) {
                Log.e("Error", e.toString())
                emit(Resource.Error(e.toString()))
            } catch (e: HttpException) {
                Log.e("Error", e.toString())
                emit(Resource.Error(e.toString()))
            }
            emit(Resource.Loading(false))
        }
    }

    override fun getPersonalMovies(idPerson: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val personMovies = movieApi.getPersonCredits(idPerson).cast.map { cast ->  cast.toMovie() }
                emit(Resource.Success(personMovies))
            } catch (e: IOException) {
                emit(Resource.Error(e.toString()))
            } catch (e: HttpException) {
                emit(Resource.Error(e.toString()))
            }
            emit(Resource.Loading(false))
        }
    }

}