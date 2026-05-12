package com.erik.canseco.movies.data.remote.response

import com.erik.canseco.movies.data.local.entity.GenreEntity

data class GenreDto(
    val genres: List<GenreRow> = emptyList()
)

data class GenreRow(
    val id: Int = 0,
    val name: String = ""
)