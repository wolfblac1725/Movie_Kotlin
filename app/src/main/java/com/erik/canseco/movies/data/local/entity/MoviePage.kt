package com.erik.canseco.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviePage(
    @PrimaryKey
    val category: String,
    val page: Int,
    val totalPages: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
