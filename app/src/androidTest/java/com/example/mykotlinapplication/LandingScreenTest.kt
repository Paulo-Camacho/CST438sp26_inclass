package com.example.mykotlinapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

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
                onSearchGames = {},
                onSignOut = {}
            )
        }

        composeTestRule.onNodeWithText("FreeToGame").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Games").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun landingScreen_searchGames_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            LandingScreen(
                onSearchGames = { clicked = true },
                onSignOut = {}
            )
        }

        composeTestRule.onNodeWithText("Search Games").performClick()
        assertTrue(clicked)
    }
}