package com.example.mykotlinapplication.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_table")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameId: Int,
    val rating: Int,          // 0..5 (0 means “no rating, comment only”)
    val comment: String,      // can be empty
    val createdAt: Long = System.currentTimeMillis()
)
