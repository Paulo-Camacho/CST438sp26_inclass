package com.example.mykotlinapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

//Test for text input on the search bar
class GamesSearchTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gamesScreen_searchBar_isDisplayedAndTakesInput() {
        val vm = GamesViewModel()
        
        composeTestRule.setContent {
            GamesScreen(
                onBack = {},
                vm = vm
            )
        }

        // Making sure the search bar is there
        composeTestRule.onNodeWithText("Search games by title...").assertIsDisplayed()

        // This checks whether the search bar is taking input
        val searchText = "Overwatch"
        composeTestRule.onNodeWithText("Search games by title...").performTextInput(searchText)

        assert(vm.searchQuery.value == searchText)
    }
}
