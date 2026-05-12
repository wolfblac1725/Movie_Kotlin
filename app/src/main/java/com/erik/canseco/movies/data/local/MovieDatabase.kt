package com.erik.canseco.movies.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.erik.canseco.movies.data.local.entity.CastEntity
import com.erik.canseco.movies.data.local.entity.GenreEntity
import com.erik.canseco.movies.data.local.entity.MovieEntity
import com.erik.canseco.movies.data.local.entity.MovieKey
import com.erik.canseco.movies.data.local.entity.MoviePage

@Database(
    entities = [MovieEntity::class, MovieKey::class, CastEntity::class, MoviePage::class, GenreEntity::class],
    version = 4,
    autoMigrations =[
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ]
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}