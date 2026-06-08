package com.erik.canseco.movies.data.remote.response

data class MovieListDto(
    val page: Int = 0,
    val results: List<MovieDto> = emptyList(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)