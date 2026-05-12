package com.erik.canseco.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erik.canseco.movies.domain.model.Genre

@Entity
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)
