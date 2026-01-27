package com.example.mykotlinapplication

import retrofit2.http.GET

interface FreeToGameApi {
    @GET("api/games")
    suspend fun getGames(): List<Game>
}