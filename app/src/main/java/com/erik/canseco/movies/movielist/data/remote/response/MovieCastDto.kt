package com.erik.canseco.movies.movielist.data.remote.response

data class MovieCastDto(
    val cast: List<CastDto>,
    val crew: List<CrewDto>,
    val id: Int
)