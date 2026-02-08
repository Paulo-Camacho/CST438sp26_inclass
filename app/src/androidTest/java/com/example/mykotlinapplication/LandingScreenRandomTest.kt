package com.example.mykotlinapplication

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class LandingScreenRandomTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun randomButton_showsRandomCard() {
        // A small test Game to act as the API-supplied random suggestion
        val testGame = Game(
            id = 999,
            title = "Test Game Title",
            thumbnail = "https://example.com/test.png",
            genre = "TestGenre",
            platform = "PC"
        )

        // Simulate ViewModel state for randomGame using a mutableState
        val randomState = mutableStateOf<Game?>(null)

        composeTestRule.setContent {
            LandingScreen(
                randomGame = randomState.value,
                popularGames = emptyList(),
                onSearchGames = { /* no-op */ },
                onRandomRequested = {
                    // Simulate the ViewModel picking a random game
                    randomState.value = testGame
                },
                onClearRandom = {
                    randomState.value = null
                },
                onSignOut = { /* no-op */ },
                onGameClick = { /* no-op */ }
            )
        }

        // Click the "Random Game" button which should set randomState -> testGame
        composeTestRule.onNodeWithText("Random Game").performClick()

        // After the callback runs, the LandingScreen should recompose and display the card's title
        composeTestRule.onNodeWithText("Test Game Title").assertIsDisplayed()

    }
}