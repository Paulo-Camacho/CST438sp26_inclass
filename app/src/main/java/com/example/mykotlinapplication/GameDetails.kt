package com.example.mykotlinapplication

data class GameDetails(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val genre: String,
    val platform: String,
    val publisher: String? = null,
    val developer: String? = null,
    val release_date: String? = null,
    val description: String? = null,
    val game_url: String? = null
)
