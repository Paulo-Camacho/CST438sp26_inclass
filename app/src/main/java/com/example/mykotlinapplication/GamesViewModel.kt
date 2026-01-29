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
    private val _selectedDescriptionofGame = MutableStateFlow<Description_of_Game?>(null)
    val selectedDescriptionofGame: StateFlow<Description_of_Game?> = _selectedDescriptionofGame

    init {
    var sortBy: String? = null
    var category: String? = null

    fun fetchGames() {
        viewModelScope.launch {
            try {
                _games.value = RetrofitInstance.api.getGames(sortBy, category)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun openGameDetails(id: Int) {
        viewModelScope.launch {
            try {
                _error.value = null
                _selectedDescriptionofGame.value = null // show loading state
                _selectedDescriptionofGame.value = RetrofitInstance.api.getGameDetails(id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun closeGameDetails() {
        _selectedDescriptionofGame.value = null
    }
}
    init {
        fetchGames()
    }
}
