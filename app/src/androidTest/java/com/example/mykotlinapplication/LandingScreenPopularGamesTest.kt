package com.example.mykotlinapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class LandingScreenPopularGamesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun popularGames_empty_showsLoadingMessage() {
        composeTestRule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingScreen(
                        randomGame = null,
                        popularGames = emptyList(),
                        onSearchGames = {},
                        onRandomRequested = {},
                        onClearRandom = {},
                        onSignOut = {},
                        onGameClick = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Top 10 Popular Games").assertExists()
        composeTestRule.onNodeWithText("Loading popular games...").assertExists()
    }

    @Test
    fun popularGames_nonEmpty_displaysGameTitles() {
        val games = listOf(
            Game(
                id = 1,
                title = "Popular Game 1",
                thumbnail = "https://example.com/1.png",
                genre = "Shooter",
                platform = "PC (Windows)"
            ),
            Game(
                id = 2,
                title = "Popular Game 2",
                thumbnail = "https://example.com/2.png",
                genre = "MMORPG",
                platform = "Web Browser"
            )
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingScreen(
                        randomGame = null,
                        popularGames = games,
                        onSearchGames = {},
                        onRandomRequested = {},
                        onClearRandom = {},
                        onSignOut = {},
                        onGameClick = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Top 10 Popular Games").assertExists()
        composeTestRule.onNodeWithText("Popular Game 1").assertExists()
        composeTestRule.onNodeWithText("Popular Game 2").assertExists()
    }

    @Test
    fun popularGames_clickingCard_callsOnGameClickWithCorrectId() {
        val games = listOf(
            Game(
                id = 42,
                title = "Clickable Game",
                thumbnail = "https://example.com/click.png",
                genre = "Strategy",
                platform = "PC (Windows)"
            )
        )

        val clickedId = AtomicInteger(-1)

        composeTestRule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingScreen(
                        randomGame = null,
                        popularGames = games,
                        onSearchGames = {},
                        onRandomRequested = {},
                        onClearRandom = {},
                        onSignOut = {},
                        onGameClick = { id -> clickedId.set(id) }
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Clickable Game").performClick()
        assertEquals(42, clickedId.get())
    }
}