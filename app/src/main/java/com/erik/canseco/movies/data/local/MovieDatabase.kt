package com.erik.canseco.movies.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.erik.canseco.movies.data.local.entity.CastEntity
import com.erik.canseco.movies.data.local.entity.MovieEntity
import com.erik.canseco.movies.data.local.entity.MovieKey

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