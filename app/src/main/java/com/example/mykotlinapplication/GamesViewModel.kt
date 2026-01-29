package com.example.mykotlinapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // NEW: selected game details
    private val _selectedGameDetails = MutableStateFlow<GameDetails?>(null)
    val selectedGameDetails: StateFlow<GameDetails?> = _selectedGameDetails

    init {
        viewModelScope.launch {
            try {
                _games.value = RetrofitInstance.api.getGames()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun openGameDetails(id: Int) {
        viewModelScope.launch {
            try {
                _error.value = null
                _selectedGameDetails.value = null // show loading state
                _selectedGameDetails.value = RetrofitInstance.api.getGameDetails(id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun closeGameDetails() {
        _selectedGameDetails.value = null
    }
}
