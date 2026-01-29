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

    init {
        fetchGames()
    }
}