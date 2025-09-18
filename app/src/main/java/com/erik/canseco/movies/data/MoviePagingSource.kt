package com.erik.canseco.movies.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erik.canseco.movies.data.remote.MovieApi
import com.erik.canseco.movies.domain.model.Movie
import com.erik.canseco.movies.utility.TypeShared
import com.erik.canseco.movies.utility.UtilityFun
import com.erik.canseco.movies.utility.mappers.toMovie
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val movieApi: MovieApi,
    private val type: TypeShared
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,Movie> {
        val page = params.key ?: 1
        return try {
            val category = UtilityFun.toCategory(type)
            val response = movieApi.getMoviesList(category, page)

            val result = response.results.map {it.toMovie(type.toString()) }
            LoadResult.Page(
                data = result,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}