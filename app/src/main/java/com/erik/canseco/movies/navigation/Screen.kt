package com.erik.canseco.movies.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Detail(
    val movie: String
)