package com.erik.canseco.movies.data.remote.response

data class MovieVideosDto(
    val results: List<MovieVideoDto> = emptyList()
)

data class MovieVideoDto(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val site: String = "",
    val type: String = ""
)
