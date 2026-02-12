package com.example.mykotlinapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val games: StateFlow<List<Game>> = combine(_games, _searchQuery) { gamesList, query ->
        if (query.isBlank()) {
            gamesList
        } else {
            gamesList.filter { it.title.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedDescriptionofGame = MutableStateFlow<Description_of_Game?>(null)
    val selectedDescriptionofGame: StateFlow<Description_of_Game?> = _selectedDescriptionofGame

    // NEW: random suggestion
    private val _randomGame = MutableStateFlow<Game?>(null)
    val randomGame: StateFlow<Game?> = _randomGame

    // ✅ These must be class members (so MainActivity.kt can access them)
    var sortBy: String? = null
    var category: String? = null

    init {
        fetchGames()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun fetchGames() {
        viewModelScope.launch {
            try {
                _error.value = null
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
                _selectedDescriptionofGame.value = RetrofitInstance.api.getGameDetails(id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun closeGameDetails() {
        _selectedDescriptionofGame.value = null
    }

    /**
     * Pick a random game to suggest. Prefer the already-fetched list (_games).
     * If _games is empty (not loaded), attempt to fetch trending/popular list.
     */
    fun pickRandomGame() {
        viewModelScope.launch {
            try {
                // If we already have games loaded, pick from them
                val source = _games.value.ifEmpty {
                    // Fallback: request popular (trending) list from API
                    RetrofitInstance.api.getGames(sortBy = "popularity")
                }

                if (source.isNotEmpty()) {
                    _randomGame.value = source.random()
                } else {
                    _randomGame.value = null
                }
            } catch (e: Exception) {
                // keep random empty and surface error to _error if desired
                _error.value = e.message ?: "Failed to load random game"
                _randomGame.value = null
            }
        }
    }

    fun clearRandom() {
        _randomGame.value = null
    }
}