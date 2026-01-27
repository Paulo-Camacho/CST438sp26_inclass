package com.example.mykotlinapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.freetogame.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: FreeToGameApi = retrofit.create(FreeToGameApi::class.java)
}
