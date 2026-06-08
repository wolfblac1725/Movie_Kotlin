package com.erik.canseco.movies.utility.mappers

import com.erik.canseco.movies.data.local.entity.GenreEntity
import com.erik.canseco.movies.data.remote.response.GenreRow
import com.erik.canseco.movies.domain.model.Genre

fun GenreRow.toGenreEntity(): GenreEntity {
    return GenreEntity(
        id = id ?: -1 ,
        name = name ?: "",
    )
}
fun GenreEntity.toGenre(): Genre{
    return Genre(
        id = id,
        name = name
    )
}