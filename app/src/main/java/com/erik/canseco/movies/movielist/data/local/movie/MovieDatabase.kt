package com.erik.canseco.movies.movielist.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, MovieKey::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase :RoomDatabase() {
    abstract val movieDao: MovieDao
}