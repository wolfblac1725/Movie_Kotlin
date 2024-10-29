package com.erik.canseco.movies.movielist.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun updateMovieList(movieList: List<MovieEntity>)
    @Upsert
    suspend fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM MovieEntity where id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%'")
    suspend fun getMoviesListByCategory(category: String): List<MovieEntity>
}