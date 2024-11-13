package com.erik.canseco.movies.movielist.domain.model

data class Cast(
    val id: Int,
    val idMovie: Int,
    val adult: Boolean,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)