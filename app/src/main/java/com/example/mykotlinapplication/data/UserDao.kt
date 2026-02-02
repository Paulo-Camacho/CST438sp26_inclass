package com.example.mykotlinapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//This contains the queries for the database.
@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)
}