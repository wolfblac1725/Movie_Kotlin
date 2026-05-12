package com.erik.canseco.movies.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.erik.canseco.movies.data.local.entity.CastEntity
import com.erik.canseco.movies.data.local.entity.GenreEntity
import com.erik.canseco.movies.data.local.entity.MovieEntity
import com.erik.canseco.movies.data.local.entity.MovieKey
import com.erik.canseco.movies.data.local.entity.MoviePage

@Dao
interface MovieDao {
    @Upsert
    suspend fun updateMovieList(movieList: List<MovieEntity>)
    @Upsert
    suspend fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM MovieEntity where id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%'")
    suspend fun getMoviesListByCategory(category: String): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%'")
    fun getMoviesListCategoryPagination(category: String): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%' order by popularity desc")
    fun getMoviesListCategoryPaginationPopular(category: String): PagingSource<Int, MovieEntity>
    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%' order by vote_average desc")
    fun getMoviesListCategoryPaginationToRated(category: String): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM MovieEntity where category LIKE '%'||:category||'%' order by release_date desc")
    fun getMoviesListCategoryPaginationNowPlaying(category: String): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM MovieEntity")
    fun clearMovieList()

    @Query("SELECT count(*) FROM MovieEntity where category LIKE '%'||:category||'%'")
    suspend fun getCountMoviesByCategory(category: String): Int

    @Upsert
    suspend fun updateMovieKey(movieKey: MovieKey)

    @Query("SELECT * FROM MovieKey where id = :category ")
    suspend fun getMovieKey(category: String): List<MovieKey>

    @Query("SELECT * FROM CastEntity where idMovie = :idMovie")
    suspend fun getCastByMovieId(idMovie: Int): List<CastEntity>

    @Upsert
    suspend fun updateCast(cast: List<CastEntity>)

    // Nuevas funciones para el borrado inteligente por categoría
    @Query("SELECT * FROM MovieEntity WHERE category LIKE '%' || :categoryToFilter || '%'")
    suspend fun getMoviesAssociatedWithCategory(categoryToFilter: String): List<MovieEntity>

    @Query("DELETE FROM MovieEntity WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)

    @Transaction
    suspend fun clearMoviesForCategory(categoryToClear: String) {
        val moviesToProcess = getMoviesAssociatedWithCategory(categoryToClear)
        for (movie in moviesToProcess) {
            val categories = movie.category.split(',').map { it.trim() }.toMutableList()
            if (categories.contains(categoryToClear)) {
                categories.remove(categoryToClear)
                if (categories.isEmpty()) {
                    deleteMovieById(movie.id)
                } else {
                    updateMovie(movie.copy(category = categories.joinToString(",")))
                }
            }
        }
    }
    @Transaction
    suspend fun updateMovieListCategory(movieList: List<MovieEntity>) {
        for(movie in movieList){
            val movieData = getMovieById(movie.id)
            if(movieData == null)
                updateMovie(movie)
            else
                updateMovie(movieData.copy(category = movieData.category.plus(",").plus(movie.category)))
        }
    }

    // En MovieDao.kt
    @Query("DELETE FROM MovieKey WHERE id = :category")
    suspend fun deleteMovieKeyByCategory(category: String)

    @Query("SELECT lastUpdated FROM MoviePage WHERE category = :category")
    suspend fun lastUpdated(category: String): Long

    @Query("DELETE FROM MoviePage WHERE category = :category")
    suspend fun clearMoviePageByCategory(category: String)

    @Upsert
    suspend fun updateMoviePage(moviePage: MoviePage)

    @Query("SELECT * FROM MoviePage WHERE category = :category")
    suspend fun getMoviePageByCategory(category: String): MoviePage?

    @Query("SELECT page FROM MoviePage WHERE category = :category")
    suspend fun getPageByCategory(category: String): Int?

    @Query("SELECT COUNT(*) FROM GenreEntity")
    suspend fun getCountGenres(): Int

    @Upsert
    suspend fun updateGenres(genre: List<GenreEntity>)
    @Query("SELECT * FROM GenreEntity where id in (:genres)")
    suspend fun getGenres(genres: List<Int>): List<GenreEntity>

}