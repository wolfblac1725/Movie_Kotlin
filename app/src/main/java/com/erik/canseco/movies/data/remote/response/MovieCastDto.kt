package com.erik.canseco.movies.data.remote.response

data class MovieCastDto(
    val cast: List<CastDto> = emptyList(),
    val id: Int = 0
)