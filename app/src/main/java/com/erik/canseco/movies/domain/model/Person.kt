package com.erik.canseco.movies.domain.model

data class Person(
    val id: Int,
    val alsoKnownAs: List<String>? = null,
    val name: String? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val deathday: String? = null,
    val gender: Int? = null,
    val homepage: String? = null,
    val imdbId: String? = null,
    val placeOfBirth: String? = null,
    val popularity: Double? = null,
    val profilePath: String ? = null
)