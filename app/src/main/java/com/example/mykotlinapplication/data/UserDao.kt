package com.example.mykotlinapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    //  Used by AuthViewModel.login()
    @Query("""
        SELECT * FROM user_table
        WHERE username = :username
        AND password = :password
        LIMIT 1
    """)
    suspend fun login(username: String, password: String): User?


    @Query("SELECT * FROM user_table")
    suspend fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)
}
