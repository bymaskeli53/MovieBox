package com.example.moviebox.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE movie ADD COLUMN backdropPath TEXT")
            database.execSQL("ALTER TABLE movie ADD COLUMN voteAverage REAL")
            database.execSQL("ALTER TABLE movie ADD COLUMN voteCount INTEGER")
            database.execSQL("ALTER TABLE movie ADD COLUMN popularity REAL")
            database.execSQL("ALTER TABLE movie ADD COLUMN adult INTEGER")
            database.execSQL("ALTER TABLE movie ADD COLUMN video INTEGER")
            database.execSQL("ALTER TABLE movie ADD COLUMN originalLanguage TEXT")
            database.execSQL("ALTER TABLE movie ADD COLUMN originalTitle TEXT")
        }
    }
}
