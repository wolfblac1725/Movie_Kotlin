package com.erik.canseco.movies.movielist.data.remote

import com.erik.canseco.movies.movielist.data.remote.response.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("language") lenguage: String = "es-ES",
        @Query("api_key") apiKey: String = API_KEY
    ):MovieListDto

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "24b0da270f7f7499fe98810b509b6b8f"
    }
}