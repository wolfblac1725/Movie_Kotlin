package com.erik.canseco.movies.movielist.data.mappers

import com.erik.canseco.movies.movielist.data.local.movie.CastEntity
import com.erik.canseco.movies.data.remote.response.CastDto
import com.erik.canseco.movies.domain.model.Cast

fun CastDto.toCastEntity(
    idMovie: Int
): CastEntity {
    return CastEntity(
        id = id ?: -1 ,
        idMovie = idMovie ?: -1 ,
        adult = adult ?: false,
        cast_id = cast_id ?: -1,
        character = character ?: "",
        credit_id = credit_id ?: "",
        gender = gender ?: -1,
        known_for_department = known_for_department ?: "",
        name = name ?: "",
        order = order ?: -1,
        original_name = original_name ?: "",
        popularity = popularity ?: 0.0,
        profile_path = profile_path ?: ""
    )
}
fun CastDto.toCast(): Cast {
    return Cast(
        id ?: -1 ,
        id ?: -1 ,
        adult ?: false,
        cast_id ?: -1 ,
        character ?: "",
        credit_id ?: "",
        gender ?: -1 ,
        known_for_department ?: "",
        name ?: "",
        order ?: -1 ,
        original_name ?: "",
        popularity ?: 0.0,
        profile_path ?: ""
    )
}
fun CastEntity.toCast(
    idMovie: Int
): Cast {
    return Cast(
        id ?: -1 ,
        idMovie ?: -1 ,
        adult ?: false,
        cast_id ?: -1 ,
        character ?: "",
        credit_id ?: "",
        gender ?: -1 ,
        known_for_department ?: "",
        name ?: "",
        order ?: -1 ,
        original_name ?: "",
        popularity ?: 0.0,
        profile_path ?: ""
    )
}