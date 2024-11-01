package com.erik.canseco.movies.movielist.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erik.canseco.movies.movielist.data.mappers.toMovie
import com.erik.canseco.movies.movielist.data.mappers.toMovieEntity
import com.erik.canseco.movies.movielist.data.remote.MovieApi
import com.erik.canseco.movies.movielist.domain.model.Movie
import okio.IOException
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(
    private val api: MovieApi,
    private val category: String
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1

            val response = api.getMoviesList(category, page)

            val movieList = response.results.map { it.toMovieEntity(category) }


            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (response.total_pages >= page + 1 ) page + 1 else null

            LoadResult.Page(
                data = movieList.map { it.toMovie(category)},
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}