package com.erik.canseco.movies.movielist.data.local.movie

import androidx.paging.PagingSource
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

    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%'")
    fun getMoviesListCategoryPagination(category: String): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM MovieEntity")
    fun clearMovieList()

    @Query("SELECT count(*) FROM MovieEntity where category LIKE '%'||:category||'%'")
    suspend fun getCountMoviesByCategory(category: String): Int

    @Upsert
    suspend fun updateMovieKey(movieKey: MovieKey)

    @Query("SELECT * FROM MovieKey where id = :category ")
    suspend fun getMovieKey(category: String): List<MovieKey>


}