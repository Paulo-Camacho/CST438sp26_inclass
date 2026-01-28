package com.example.mykotlinapplication
import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToGameApi {
    @GET("api/games")
    suspend fun getGames(
        @Query("sort-by") sortBy: String? = null,
        @Query("category") category: String? = null
    ): List<Game>
}