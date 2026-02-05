package com.example.mykotlinapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review_table WHERE gameId = :gameId ORDER BY createdAt DESC")
    fun observeReviews(gameId: Int): Flow<List<Review>>

    @Query("SELECT AVG(rating) FROM review_table WHERE gameId = :gameId AND rating > 0")
    fun observeAverageRating(gameId: Int): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: Review)
}
