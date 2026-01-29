package com.example.mykotlinapplication

import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToGameApi {
    @GET("api/games")
    suspend fun getGames(): List<Game>

    // NEW: details endpoint
    @GET("api/game")
    suspend fun getGameDetails(@Query("id") id: Int): GameDetails
}
