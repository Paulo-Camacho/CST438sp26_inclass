package com.example.mykotlinapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * UI tests for the LandingScreen composable.
 *
 * These tests verify that the landing page displays the expected title and action buttons,
 * and that user interactions (such as tapping the "Search Games" button) correctly trigger
 * the provided callback functions.
 *
 * This provides basic coverage for the landing page introduced in Issue #4.
 */

class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun landingScreen_displaysTitleAndButtons() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("FreeToGame").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Games").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun landingScreen_searchGames_callsCallback() {
        val clicked = AtomicBoolean(false)

        composeTestRule.setContent {
            LandingScreen(
                randomGame = null,
                popularGames = emptyList(),
                onSearchGames = { clicked.set(true) },
                onRandomRequested = {},
                onClearRandom = {},
                onSignOut = {},
                onGameClick = {}
            )
        }

        composeTestRule.onNodeWithText("Search Games").performClick()
        // run assertions after the click; AtomicBoolean is thread-safe
        assert(clicked.get())
    }
}