package com.erik.canseco.movies.data.remote.response

data class PersonDto(
    val adult: Boolean = false,
    val also_known_as: List<String> = emptyList(),
    val biography: String = "",
    val birthday: String = "",
    val deathday: String = "",
    val gender: Int = 0,
    val homepage: String = "",
    val id: Int = 0,
    val imdb_id: String = "",
    val known_for_department: String = "",
    val name: String = "",
    val place_of_birth: String = "",
    val popularity: Double = 0.0,
    val profile_path: String = ""
)