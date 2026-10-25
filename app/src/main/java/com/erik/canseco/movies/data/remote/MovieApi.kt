package com.erik.canseco.movies.data.remote

import com.erik.canseco.movies.BuildConfig
import com.erik.canseco.movies.data.remote.response.CombinedCreditPersonDto
import com.erik.canseco.movies.data.remote.response.GenreDto
import com.erik.canseco.movies.data.remote.response.MovieCastDto
import com.erik.canseco.movies.data.remote.response.MovieDetailDto
import com.erik.canseco.movies.data.remote.response.MovieListDto
import com.erik.canseco.movies.data.remote.response.PersonDto
import com.erik.canseco.movies.data.remote.response.MovieVideosDto
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

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetailDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int
    ): MovieVideosDto

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreDto

    @GET ("person/{person_id}")
    suspend fun getPersonDetail(
        @Path("person_id") personId: Int
    ): PersonDto

    @GET("person/{person_id}/combined_credits")
    suspend fun getPersonCredits(
        @Path("person_id") personId: Int
    ): CombinedCreditPersonDto

}
