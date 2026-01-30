package com.example.mykotlinapplication

import org.junit.Assert.assertEquals
import org.junit.Test

class DescriptionOfGameTest {

    @Test
    fun descriptionOfGame_storesValuesCorrectly() {
        val game = Description_of_Game(
            id = 1,
            title = "Test Game",
            thumbnail = "http://example.com/image.png",
            genre = "RPG",
            platform = "PC",
            publisher = "Test Publisher",
            developer = "Test Developer",
            release_date = "2024-01-01",
            description = "This is a test game",
            game_url = "http://example.com"
        )

        assertEquals(1, game.id)
        assertEquals("Test Game", game.title)
        assertEquals("RPG", game.genre)
        assertEquals("PC", game.platform)
        assertEquals("Test Publisher", game.publisher)
        assertEquals("Test Developer", game.developer)
        assertEquals("2024-01-01", game.release_date)
        assertEquals("This is a test game", game.description)
        assertEquals("http://example.com", game.game_url)
    }
}
