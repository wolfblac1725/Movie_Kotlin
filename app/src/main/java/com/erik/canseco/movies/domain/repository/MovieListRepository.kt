package com.erik.canseco.movies.domain.repository

import androidx.paging.PagingData
import com.erik.canseco.movies.data.remote.response.PersonDto
import com.erik.canseco.movies.domain.model.Cast
import com.erik.canseco.movies.domain.model.Genre
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.domain.model.Person
import com.erik.canseco.movies.utility.Resource
import com.erik.canseco.movies.utility.TypeShared
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    fun getMovieListPaging(type: TypeShared): Flow<PagingData<Movie>>
    fun getMovieListPaging2(type: TypeShared): Flow<PagingData<Movie>>
    fun getMovieCastDetail(idMovie: Int): Flow<Resource<List<Cast>>>
    //fun getMovieDetail(idMovie: Int): Flow<Resource<Movie>>
    suspend fun updateGenres()
    fun getGenres(genres: List<Int>): Flow<Resource<List<Genre>>>

    fun getPersonal (idPerson: Int): Flow<Resource<Person>>

    fun getPersonalMovies(idPerson: Int): Flow<Resource<List<Movie>>>
}