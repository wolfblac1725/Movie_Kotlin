package com.erik.canseco.movies.movielist.data.remote

import com.erik.canseco.movies.movielist.data.remote.response.MovieCastDto
import com.erik.canseco.movies.movielist.data.remote.response.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
    ): MovieListDto

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int
    ): MovieCastDto
    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "24b0da470f7f7499fe95810b709b3b8f"
        const val LANGUAGE = "es-ES"
    }
}
