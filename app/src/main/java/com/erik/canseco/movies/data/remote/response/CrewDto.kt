package com.erik.canseco.movies.data.remote.response

data class CrewDto(
    val id: Int = 0,
    val adult: Boolean = false,
    val credit_id: String = "",
    val department: String = "",
    val gender: Int = 0,
    val job: String = "",
    val known_for_department: String = "",
    val name: String = "",
    val original_name: String = "",
    val popularity: Double = 0.0,
    val profile_path: String? = null
)