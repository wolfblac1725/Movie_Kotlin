package com.erik.canseco.movies.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["id", "idMovie"])
data class CastEntity(
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
    val profile_path: String
)