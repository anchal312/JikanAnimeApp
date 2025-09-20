package com.example.jikananime.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,
    val imageUrl: String?,
    val trailerUrl: String?,
    val genres: List<String> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
)
