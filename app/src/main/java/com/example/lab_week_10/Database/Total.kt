package com.example.lab_week_10.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "total")
data class Total(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 1, // hanya satu record
    @Embedded
    val total: TotalObject
)

data class TotalObject(
    @ColumnInfo(name = "value")
    val value: Int,

    @ColumnInfo(name = "date")
    val date: String
)
