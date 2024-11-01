package com.erik.canseco.movies.movielist.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieKey(
    @PrimaryKey (autoGenerate = false)
    var id: String,
    var prev: Int?,
    var next: Int?
)
