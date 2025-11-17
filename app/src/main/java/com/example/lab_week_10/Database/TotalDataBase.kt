package com.example.lab_week_10.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Total::class],
    version = 1,
    exportSchema = false
)
abstract class TotalDatabase : RoomDatabase() {
    abstract fun totalDao(): TotalDao
}
