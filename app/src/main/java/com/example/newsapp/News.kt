package com.example.newsapp

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Room DB Entity Class and Data class for News List

@Entity(tableName = "news")
data class News(
    @PrimaryKey @Nullable val title: String,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "imgUrl") val imgUrl: String?,
    @ColumnInfo(name = "date") val date: String?
)
