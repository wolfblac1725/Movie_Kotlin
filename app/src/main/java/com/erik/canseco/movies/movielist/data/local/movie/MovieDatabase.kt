package com.erik.canseco.movies.movielist.data.local.movie

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, MovieKey::class, CastEntity::class],
    version = 2,
    autoMigrations =[
        AutoMigration(from = 1, to = 2),
    ]
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}