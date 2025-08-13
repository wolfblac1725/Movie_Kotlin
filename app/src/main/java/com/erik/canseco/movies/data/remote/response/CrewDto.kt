package com.erik.canseco.movies.data.remote.response

data class CrewDto(
    val id: Int,
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
)